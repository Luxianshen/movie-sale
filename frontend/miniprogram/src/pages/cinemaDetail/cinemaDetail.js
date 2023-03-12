import Taro, { Component } from '@tarojs/taro'
import { View,Image,Text} from '@tarojs/components'
import './cinemaDetail.scss'
import posPng from "../../assets/images/pos.png"
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
      dates:[],
      dataList: {},
      showDateList:[],
      dealList: []
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
      Taro.request({
        url:`baseUrl/index/cinemas/${cinemaId}/${movieId}`,
        method:'GET',
        header:{'token':token.token}
      }).then(res=>{
        if(res.statusCode == 200){
          Taro.hideLoading();
          let data = res.data.data;
          data.dates.map((item,index)=>{
            this.state.showDateList.push(this.formatDateString(item));
          });
          self.setState({
            cinemaData: data.cinema,
            movieData: data.movies,
            bg: data.movies[0].pic,
            dates: data.dates,
            dataList: data.list
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

    const self = this;
    this.setState({
      reqList:{
        movieId:item.filmId,
      },
      bg:item.pic,
      activeIndex:index,
      viewId:viewId
    });
  }
  chooseItem(index){
    this.setState({
      tabIndex:index
    });
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
    const reqList = this.state.reqList;
    url = url+`?cinemaName=${cinemaName}&showId=${showId}`;
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
  render () {
    let movieIds = [];
    let cinemaData = this.state.movieData?this.state.cinemaData:{};
    let showData = this.state.movieData?this.state.movieData:[];
    showData.map((item,index)=>{
      if(item.filmId == this.state.reqList.movieId){
        this.state.activeIndex = index;
        this.selected(item,index,'view'+item.filmId);
      }
      movieIds.push(item.filmId);
    });
    let activeIndex = this.state.activeIndex;
    let dataLists = this.state.movieData?this.state.dataList:[];
    let dateLists = this.state.dates;
    let tabIndex = this.state.tabIndex;
    let dataList = [];
    if(this.state.reqList.movieId != "123"){
      dataList = dataLists.length == 0?[]:dataLists[this.state.reqList.movieId][dateLists[tabIndex]];
    }else{
      if(movieIds.length > 0){
        this.state.reqList.movieId = movieIds[activeIndex];
        dataList = dataLists.length == 0?[]:dataLists[movieIds[activeIndex]][dateLists[tabIndex]];
      }
    }

    if(typeof(dataList) == 'undefined'){
      dataList = [];
    }

    //小吃
    let dealList = this.state.movieData? this.state.dealList:{};
    let reqList = this.state.reqList;

    return(
      <View className="cinemaDetail">
        <View className="header">
          <View className="locationInfo">
            <View className="name">{cinemaData.cinemaName}</View>
            <View className="addr">{cinemaData.address}</View>
          </View>
          <View className="locateIcon" onClick={this.navigateToMap.bind(this,"../map/map",cinemaData)}>
            <Image src={cinemaData.pic}></Image>
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
              scrollIntoView={this.state.viewId}
          >
                        {showData.map((item,index)=>{
                          return (
                              <Image  src={item.pic} key={item.filmId}  id={'view'+item.filmId} onClick={this.selected.bind(this,item,index,e.currentTarget.id)} className={ item.filmId ==  this.state.reqList.movieId?'active img':'img'}></Image>
                          );
                        })}
            </ScrollView>
        </View>
        <View className="movieInfo">
          <View className="movieName">
            {showData[activeIndex].name}<Text className="comment">{showData[activeIndex].grade *1 /10}分</Text>
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
              <View key={index} className={this.state.tabIndex == index?'selected dateItem':'dateItem'} onClick={this.chooseItem.bind(this,index)}>{item}</View>
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
                    <View className="hall">{item.hallName}</View>
                  </View>
                  <View className="sellPrice">
                      <View className="price"><Text className="mark">￥{item.settlePrice}</Text> {item.settlePrice}</View>
                      <View className="discount">{item.cutPrice}</View>
                  </View>
                  <View className="button" onClick={this.navigateSeat.bind(this,'../seat/seat',item)}>
                    购票
                  </View>
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
