import Taro, { Component } from '@tarojs/taro'
import { View,Image,Text, Button} from '@tarojs/components'
import './cinemaDetail.scss'
import posPng from "../../assets/images/pos.png"
import locationPng from "../../assets/images/location.png"
export default class CinemasDetail extends Component {
  config={
    enablePullDownRefresh:false
  }
  constructor(props){
    super(props);

    this.state = {
      reqList:{
        cinemaId:'',
        movieId:'123'
      },
      movieIds:[],
      movieData:null,
      bg:"",
      left:0,
      viewId:'',
      activeIndex:0,
      tabIndex:0,
      today:'',
      dates:[],
      dataList: [],
      allData: [],
      showDateList:[],
      dealList: [],
      phoneButton: false
    }
  }
  getCinemaDetail(){
    let params = this.$router.params;
    let movieId = params.movieId?params.movieId:'123';
    let cinemaId = params.cinemaId?params.cinemaId:"";
    const self = this;
    this.setState({
      viewId:'view'+movieId,
      reqList:{
        cinemaId:cinemaId,
        movieId:movieId
      }
    },()=>{
      Taro.showLoading({
        title:"加载数据中"
      });
      let token = Taro.getStorageSync("token");
      if(typeof(token.phone) != 'undefined'){
        this.state.phoneButton = true;
      }
      Taro.request({
        url:baseUrl + `/index/cinemas/${cinemaId}/${movieId}`,
        method:'GET',
        header:{'token':token.token}
      }).then(res=>{
        if(res.statusCode == 200){
          Taro.hideLoading();
          let data = res.data.data;
          let showDateList = [];

          let movieIds = [];
          data.movies.map((item,index)=>{
            if(item.filmId == this.state.reqList.movieId){
              this.state.activeIndex = index;
            }
            movieIds.push(item.filmId);
          });

          let activeIndex = this.state.activeIndex;
          let dataList  = data.list;
          let tabIndex = this.state.tabIndex;
          if(this.state.reqList.movieId == "123"){
            this.state.reqList.movieId = movieIds[activeIndex];
          }
          //日期
          let selecMovieData = dataList[this.state.reqList.movieId];
          let dates = [];
          Object.keys(selecMovieData).map(key => {
            dates.push(key);
            showDateList.push(this.formatDateString(key));
          });

          dataList = selecMovieData[dates[tabIndex]];
          //去掉超时数据
            if(dataList.length > 0 && dataList[0].showDate == data.dates[0]){
               dataList = dataList.filter(item => {
                 const timeDiff = new Date(item.showTime) - Date.now();
                 return timeDiff <= 1 * 60 * 60 * 1000; // Keep items with showTime within 1 hour from now
               });
            }

          self.setState({
            cinemaData: data.cinema,
            movieData: data.movies,
            bg: data.movies[0].pic,
            dates: dates,
            showDateList: showDateList,
            dataList: dataList,
            allData: data.list,
            today: data.dates[0]
          },()=>{
            Taro.setNavigationBarTitle({
              title:data.cinema.cinemaName
            })
          })
        }
      }).catch(err=>{
        console.log(err.message)
      })
    })

  }

  selected(item,index,viewId){

    let showDate = '';
    let dates = [];
    let showDateList = [];
    let movieId = item.filmId;
    let selecMovieData = this.state.allData[movieId];
    Object.keys(selecMovieData).map(key => {
      if(showDate == ''){
        showDate = key;
      }
      dates.push(key);
      showDateList.push(this.formatDateString(key));
    });

    const self = this;
    this.setState({
      reqList:{
        movieId:item.filmId,
      },
      bg:item.pic,
      activeIndex:index,
      viewId:viewId,
      dates: dates,
      showDateList: showDateList
    },()=>{
      self.chooseItem(0);
    });

  }
  chooseItem(index){
    let movieId = this.state.reqList.movieId;
    let dates = this.state.dates;
    let dataList = this.state.allData[movieId][dates[index]];
    if(typeof(dataList) == 'undefined'){
      dataList = [];
    }
    //去掉超时数据
      if(dataList.length > 0 && dataList[0].showDate == this.state.today){
         let tempList = [];
         dataList.map(item=>{
             let flag = new Date(item.showTime)- Date.now()>1*60*60*1000;
              if(flag){
                tempList.push(item);
              }
         })
         dataList = tempList;
      }
    this.setState({
      tabIndex:index,
      dataList: dataList
    });
    this.forceUpdate();
  }
  navigateToMap(url,cinemaData){
    url = url+`?lng=${cinemaData.longitude}&lat=${cinemaData.latitude}&title=${this.state.cinemaData.cinemaName}`;
    Taro.navigateTo({
      url:url
    })
  }
  navigateSeat(url,item){
    const cinemaName = this.state.cinemaData.cinemaName;
    const showId =  item.showId;
    let price = item.settlePrice;
    if (price > 20) {
      price = price - price/10 -1;
    }
    item.bg =this.state.bg;
    const reqList = this.state.reqList;
    url = url+`?cinemaName=${cinemaName}&showId=${showId}&price=${price}&item=${encodeURIComponent(JSON.stringify(item))}`;
    Taro.navigateTo({
      url:url
    })
  }
  componentDidMount () {
    this.getCinemaDetail();
  }
  formatDateString(date) {
    let dates = new Date();
    let day = dates.getDate();
    let dateParam = date.split('-');
    if (dateParam[2] == day) {
      return "今天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else if (dateParam[2] == day + 1) {
      return "明天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else if (dateParam[2] == day + 2) {
      return "后天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else {
      let weekday = new Date(date).getDay();
      let arr = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"]
      return arr[weekday] + dateParam[1] + "月" + dateParam[2] + "日";
    }
  }
  getTel = (e) => {
    console.log(e.detail);
    let self = this;
    this.setState({
      isNum: true
    })
    let {
      encryptedData,
      iv
    } = e.detail;

    if (e.detail.errMsg == "getPhoneNumber:ok") {
      let code = e.detail.code;
      let encryptedData = e.detail.encryptedData;
      let iv = e.detail.iv;
      let token = Taro.getStorageSync("token");
      let sessionKey = token.sessionKey;

      Taro.request({
        url: 'baseUrl/wx/maBindPhone', //后端url
        method: 'POST',
        header: {
          'token': token.token
        },
        data: {
          code: code,
          encryptedData: encryptedData,
          iv: iv,
          sessionKey: sessionKey
        }
      }).then(res => {
        if (res.statusCode == '200') {
          Taro.setStorageSync("token", res.data.data);
          console.log(token);
          this.state.phoneButton = true;
          self.forceUpdate();
        }
      })
    } else {
    }
  }

  render () {
    const { cinemaData,movieData,activeIndex,tabIndex,dealList,reqList,dataList,dates,phoneButton } = this.state;

    return(
      <View className="cinemaDetail">
        <View className="header">
          <View className="locationInfo">
            <View className="name">{cinemaData.cinemaName}</View>
            <View className="addr">{cinemaData.address}</View>
          </View>
          <View className="locateIcon" onClick={this.navigateToMap.bind(this,"../map/showMap",cinemaData)}>
            <Image src={locationPng}></Image>
          </View>
        </View>
        <View className="showCon">
          <Image src={this.state.bg} className="bg"></Image>
          <View className="blur"></View>
          <ScrollView className='scrollview'
              scrollX
              scrollWithAnimation
              scrollTop='0'
              style="height:130Px;"
              id="swiper"
              scrollIntoView={this.state.viewId} enable-fle
          >
                        {movieData.map((item,index)=>{
                          return (
                              <Image  src={item.pic} key={item.filmId}  id={'view'+item.filmId} onClick={this.selected.bind(this,item,index,this.currentTarget.id)} className={ item.filmId ==  this.state.reqList.movieId?'active img':'img'}></Image>
                          );
                        })}
            </ScrollView>
        </View>
        <View className="movieInfo">
          <View className="movieName">
            {movieData[activeIndex].name}<Text className="comment">{movieData[activeIndex].grade *1 /10}分</Text>
          </View>
          <View className="movieDesc"></View>
        </View>
        <ScrollView className="dateSelect"
          scrollX
          scrollWithAnimation
          scrollTop='0'
          style="height:50Px;">
          {showDateList.map((item,index)=>{
            return (
              <View key={index} className={tabIndex == index?'selected dateItem':'dateItem'} onClick={this.chooseItem.bind(this,index)}>{item}</View>
            )
          })}
        </ScrollView>
        <View className="list">
          {
            dataList.map((item)=>{
              return (
                <View className="ticketInfo" key={index}>
                  <View className="time">
                    <View className="startTime">
                      {item.showTime.substring(11,16)}
                    </View>
                    <View className="endTime"></View>
                  </View>
                  <View className="station">
                    <View className="lang">{item.language}{item.planType}</View>
                    <View className="hall">{item.hallName.substring(0,5)}</View>
                  </View>
                  <View className="sellPrice">
                      <View className="price"><Text className="mark">￥{item.settlePrice -item.settlePrice/10-1}</Text> {item.settlePrice}</View>
                      <View className="discount">已减￥{item.settlePrice/10+1}</View>
                  </View>
                  <View className="button" hidden={!phoneButton} onClick={this.navigateSeat.bind(this,'../seat/seat',item)}>
                    购票
                  </View>
                  <Button className="button" hidden={phoneButton} openType = 'getPhoneNumber' onGetPhoneNumber = {this.getTel}>
                    登陆
                  </Button>
                </View>
              )
            })
          }
        </View>
        <View className="line"></View>
        <View className="things" hidden={dealList.dealList.length == 0?true:false}>
          <View className="title">影院超值套餐</View>
         </View>
      </View>
    )
  }
}
