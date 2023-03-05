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
        movieId:''
      },
      movieData:null,
      bg:"",
      left:0,
      viewId:'',
      activeIndex:0,
      tabIndex:0,
      dates:[],
      dataList: {},
      dealList: []
    }
  }
  getCinemaDetail(){
    let params = this.$router.params;
    let movieId = params.movieId?params.movieId:'';
    let cinemaId = params.cinemaId?params.cinemaId:"";
    const self = this;
    this.setState({
      reqList:{
        cinemaId:cinemaId,
        movieId:movieId
      }
    },()=>{
      Taro.showLoading({
        title:"加载数据中"
      });
      Taro.request({
        url:`http://localhost:8080/index/cinemas/${cinemaId}/${movieId}`,
        method:'GET'
      }).then(res=>{
        if(res.statusCode == 200){
          Taro.hideLoading();
          let data = res.data.data;
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
  selected(item,index,e){
    const self = this;
    this.setState({
      reqList:{
        movieId:item.filmId,
      },
      bg:item.pic,
      activeIndex:index,
      viewId:e.currentTarget.id
    });
  }
  chooseItem(index){
    this.setState({
      tabIndex:index
    });
  }
  navigateToMap(url,cinemaData){
    url = url+`?lng=${cinemaData.longitude}&lat=${cinemaData.latitude}&title=${cinemaData.cinemaName}`;
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
  render () {
    let cinemaData = this.state.movieData?this.state.cinemaData:{};
    let showData = this.state.movieData?this.state.movieData:{};
    let activeIndex = this.state.reqList.movieId?this.state.reqList.movieId:0;
    let dataLists = this.state.movieData?this.state.dataList:[];
    let dateLists = this.state.dates;
    let tabIndex = this.state.tabIndex;
    let dataList = dataLists.length == 0?[]:dataLists[activeIndex][dateLists[tabIndex]];
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
                              <Image  src={item.pic} key={item.filmId}  id={'view'+item.filmId} onClick={this.selected.bind(this,item,index,e)} className={ item.filmId ==  this.state.reqList.movieId?'active pic':'pic'}></Image>
                          );
                        })}
            </ScrollView>
        </View>
        <View className="movieInfo">
          <View className="movieName">
            {showData[activeIndex].name}<Text className="comment">{showData[activeIndex].grade}分</Text>
          </View>
          <View className="movieDesc"></View>
        </View>
        <ScrollView className="dateSelect"
          scrollX
          scrollWithAnimation
          scrollTop='0'
          style="height:50Px;">
          {dateLists.map((item,index)=>{
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
                      {item.showTime.replace(dateLists[tabIndex], "")}
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
