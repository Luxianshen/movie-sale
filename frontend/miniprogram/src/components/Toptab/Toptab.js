import Taro, { Component } from '@tarojs/taro'
import {View,Image,ScrollView,Text} from '@tarojs/components'
import searchPng from "../../assets/images/search.png"
import ico1Png from "../../assets/images/2D.png"
import ico2Png from "../../assets/images/3D.png"
import heart from "../../assets/images/heart.png"
import "./Toptab.scss"
export default class Toptab extends Component{
  constructor(props){
    super(...arguments);
    this.state = {
          currentNavtab:0,
          name: '',
          navTab:["正在热映"],
          onList:[],
          movieIds:[],
          expectData:[],
          startIndex:0,
          lastIndex:0,
          offset:0,
    }
  }
  autoLogin(){
    //微信登录逻辑
    wx.login({
      success (res) {
        if (res.code) {
          //发起网络请求
         Taro.request({
            url: 'baseUrl/wx/maLogin/'+res.code,
            method: 'GET'
          }).then(res=>{
            if(res.statusCode =="200"){
              console.log(res)
              let token = res.data.data;
			  debugger
              Taro.setStorageSync("token",token);
            }
          });
        } else {
          console.log('登录失败！' + res.errMsg)
        }
      }
    });
  }
  switchTab(index,e){
    this.setState({
      currentNavtab:index
    });
  }
  navigate(url){
    Taro.navigateTo({ url: url })
  }
  navigateDetail(url,item,cityId){
    url = url+`?id=${item.showId}&title=${item.showName}&cityId=${cityId}`
    Taro.navigateTo({ url: url })
  }
  getMoviesOnList(){

      Taro.showLoading({
        title:"加载中"
      });
      Taro.request({
        url: 'baseUrl/index/movie',
        method:"GET"
      }).then(res=>{
        if(res.statusCode == 200){
          Taro.hideLoading();
          res.data.data.list.forEach((value)=>{
            this.state.movieIds.push(value["showId"]);
          });
          this.setState({
            onList:res.data.data.list,
            startIndex:0,
            lastIndex:30
          });
        }else{
          this.setState({
            onList:null,
            movieIds:null
          })
        }
      })

  }
  appendToList(){

  }
  getFutureMovies(){
    let self = this;
    let offset = this.state.offset;
    let expectData = self.state.expectData
    let ci = self.state.id;
    Taro.request({
      url:`https://m.maoyan.com/ajax/mostExpected?ci=${ci}&limit=10&offset=${offset}&token=`,
      method:'GET'
    }).then(res=>{
      if(res.statusCode == 200){
        let data = res.data.coming;
        offset +=10;
        self.setState({
          expectData:expectData.concat(data),
          offset:offset
        });
      }
    })
  }
  do(){
    //this.getFutureMovies();
  }
  componentDidMount(){
    this.getNowCity();
    this.autoLogin();
    this.getMoviesOnList();
    //this.getFutureMovies();
    this.getLocation();
  }
  getNowCity(){

    Taro.request({
      url: 'baseUrl/index/getNowCity',
      method:"GET"
    }).then(res=>{
      if(res.statusCode == 200){
       this.state.name = res.data;
       Taro.setStorageSync("cityName",res.data);
      }
    })
  }
  toPosition(){
    Taro.showToast({
      title: '暂不支持选择',
      icon: 'success',
      duration: 2000
    });
    return false;
    Taro.navigateTo({
      url: '../position/position'
    })
  }
  getLocation(){

     var _this = this;
      wx.getLocation({
            type: 'wgs84',
            success(res) {
              console.log("-----success location-----")
              console.log(res)

              //var latitude = res.latitude
              //var longitude = res.longitude
              // wx.openLocation({
              //   latitude,
              //   longitude,
              //   scale: 18
              // })
            },
            fail(res) {
              console.log("-----fail location-----")
              console.log(res);
              Taro.showModal({
                content: '需要授权定位才能提供更好的服务哦！',
                confirmText: '确定',
                cancelText: '取消',
                success: function (res) {
                          if (res.confirm) {
                             console.log('用户点击确定')
                             _this.getAddres();
                          } else if (res.cancel) {
                             console.log('用户点击取消')
                          }
                }
              });
            }
          });
  }
  getAddres() {
      let that = this;
      wx.openSetting({ // 打开设置界面
        success: res => {
          if (res.authSetting['scope.userLocation'] || res.authSetting['scope.userLocationBackground']) {
            // 授权成功，用户点击了使用时可获取或者使用时离开后都可获取
            wx.getLocation({ // 获取用户定位，返回经纬度等信息
              isHighAccuracy: true,
              type: 'gcj02',
              success: res => {
                let latitude = res.latitude
                let longitude = res.longitude
                let url = "https://api.map.baidu.com/geocoder/v2/";
                let params = {
                  ak: "h4uqXwClsa84XjNKvS4******", //免费去百度地图上申请一个
                  output: "json",
                  location: latitude + "," + longitude
                }
                wx.request({
                  url: url,
                  data: params,
                  success: function (res1) {
                    if (res1.errMsg == "request:ok") { // 获取位置信息成功
                      wx.setStorageSync('location', res1);
                      wx.setStorageSync('openAdd', true);
                      wx.showToast({
                        title: '授权成功',
                      })
                    } else {
                      wx.showToast({
                        title: '授权失败，请从新授权',
                        icon: 'none'
                      })
                    }
                  },
                })
              }
            })
          } else {
            // 没有允许定位权限
            wx.showToast({
              title: '您拒绝了定位权限，请从新授权',
                          icon: 'none'
                        });
                      }
                    }
                  });
                }
  render(){
    let expectData = this.state.expectData?this.state.expectData:[];
    let cityId = this.state.id;
    let cityName = Taro.getStorageSync("token").cityName;
    return (
      <View>
        <View className='top-tab flex-wrp flex-tab' >
            <View className="location"  onClick={this.toPosition.bind()} >
              {this.state.name}
              <View className="cityArrow"></View>
            </View>
            {
              this.state.navTab.map((item,index) => {
                return (<View className={this.state.currentNavtab === index ? 'toptab flex-item active' : 'toptab flex-item' } key={index} onClick={this.switchTab.bind(this,index)}>
                  {item}
                </View>)
              })
            }

        </View>
        <ScrollView scroll-y scroll-top="45" lowerThreshold='30' style='height:100vh' onScrolltolower={this.appendToList.bind(this)} scrollWithAnimation>
          <View className="tabItemContent" hidden={this.state.currentNavtab === 0?false:true}>
            {this.state.onList.map((item,index)=>{
              return (
                <View className="dataItem" key={index} onClick={this.navigateDetail.bind(this,'../detail/detail',item,cityId)}>
                  <View className="leftItem">
                    <Image src={item.backgroundPicture}></Image>
                  </View>
                  <View className="rightItem">
                    <View className="itemContent">
                      <View className="title">
                        <Text>{item.showName}</Text>
                        <View className="icon">
                          {item.showMark.includes('3D')?<Image src={ico2Png}></Image>:<Image src={ico1Png}></Image>}
                        </View>
                      </View>
                      {item.globalReleased?<View className="comment smallFont">观众评 <Text className="yellow">{item.remark}</Text></View>:<View className="comment smallFont"><Text className="yellow">{item.wish}</Text>人想看</View>}
                      <View className="person smallFont">主演: {item.leadingRole}</View>
                      <View className="showInfo smallFont">{item.showInfo}</View>
                    </View>
                    <View className="operate">
                      {item.showst === 4?<view className="preBuy">预售</view>:<view className="buyTicket">购票</view>}
                    </View>
                  </View>
                </View>
              )
            })}
          </View>
          <View className="tabItemContent" hidden={this.state.currentNavtab === 1?false:true}>
              <View className="recent">近期最受期待</View>
              <ScrollView scrollX style='height:160Px' className='expect' scrollTop='0' lowerThreshold='10'
                onScrolltolower={this.do.bind(this)}
              >
                {expectData.map(item=>{
                  return (
                    <View className="picItem" key={item.id} onClick={this.navigateDetail.bind(this,'../content/content',item,)}>
                      <Image src={heart}  className="bg"></Image>
                      <Image src={item.backgroundPicture} className="poster"></Image>
                      <View className='wish'>{item.wish}人想看</View>
                      <View className='title'>{item.showName}</View>
                      <View className="time">{item.comingTitle}</View>
                    </View>
                  )
                })}
              </ScrollView>
              <View className="line"></View>
              <View className="movieContainer">
              {this.state.onList.map((item,index)=>{
                return (
                  <View className="dataItem" key={index} onClick={this.navigateDetail.bind(this,'../content/content',item,)}>
                    <View className="leftItem">
                      <Image src={item.poster}></Image>
                    </View>
                    <View className="rightItem">
                      <View className="itemContent">
                        <View className="title">
                          <Text>{item.showName}</Text>
                          <View className="icon">
                            {item.showMark.includes('3D')?<Image src={ico2Png}></Image>:<Image src={ico1Png}></Image>}
                          </View>
                        </View>
                        <View className="person smallFont">主演: {item.leadingRole}</View>
                        <View className="showInfo smallFont">{item.showInfo}</View>
                      </View>
                      <View className="operate">
                        {item.showst === 4?<view className="preBuy">预售</view>:<view className="buyTicket">购票</view>}
                      </View>
                    </View>
                  </View>
                )
              })}
              </View>
          </View>
        </ScrollView>
      </View>
    )
  }
}
