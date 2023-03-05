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
    let data = Taro.getStorageSync("cities");
    this.state = {
      currentNavtab:0,
      name:data.geoCity?data.geoCity.nm:'广州',
      id:data.geoCity?data.geoCity.id:'20',
      navTab:["正在热映","即将上映"],
      onList:[],
      movieIds:[],
      expectData:[],
      startIndex:0,
      lastIndex:0,
      offset:0,
    }
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
    url = url+`?id=${item.id}&title=${item.showName}&cityId=${cityId}`
    Taro.navigateTo({ url: url })
  }
  getMoviesOnList(){
    let cityId = this.state.id
    Taro.showLoading({
      title:"加载中"
    });
    Taro.request({
      url:"http://127.1.0.1:8080/index/movie",
      method:"GET"
    }).then(res=>{
      if(res.statusCode == 200){
        Taro.hideLoading();
        debugger
        res.data.data.list.forEach((value)=>{
          this.state.movieIds.push(value["id"]);
        });
        this.setState({
          onList:res.data.data.list,
          startIndex:0,
          //startIndex:res.data.data.list.length,
          lastIndex:30,
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
    Taro.showLoading({title: '加载中'})
    let url = "https://m.maoyan.com/ajax/moreComingList?token=&movieIds=";
    let startIndex = this.state.startIndex;
    let lastIndex = this.state.lastIndex;
    if(startIndex >= lastIndex){
      Taro.showToast({
        title: '没有更多数据了',
        icon: 'success',
        duration: 2000
      });
      return false;
    }
    for(let i = 0;i<10;i++){
      if(startIndex <= lastIndex && this.state.movieIds[startIndex]){
        if(i == 9){
          url = url +this.state.movieIds[startIndex];
        }else{
          if(this.state.movieIds[startIndex+1]){
            url = url +this.state.movieIds[startIndex]+',';
          }else{
            url = url +this.state.movieIds[startIndex];
          }
        }
        startIndex ++;
      }
    }
    Taro.request({
      url:url,
      method:'GET'
    }).then(res=>{
      let self = this;
      if(res.statusCode == 200){
        Taro.hideLoading();
        res.data.coming.forEach((value)=>{
          let arr = value["img"].split("w.h");
          value["img"] = arr[0]+"128.180"+  arr[1]
        });
        this.setState({
          onList:self.state.onList.concat(res.data.coming),
          startIndex :startIndex,
        });
      }
    })
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
        data.forEach(value=>{
          let arr = value["img"].split("w.h");
          value['img'] = arr[0]+'128.180'+arr[1]
        })
        self.setState({
          expectData:expectData.concat(data),
          offset:offset
        });
      }
    })
  }
  do(){
    this.getFutureMovies();
  }
  componentDidMount(){
    this.getMoviesOnList();
    this.getFutureMovies();
  }
  render(){
    let expectData = this.state.expectData?this.state.expectData:[];
    let cityId = this.state.id;
    return (
      <View>
        <View className='top-tab flex-wrp flex-tab' >
            <View className="location" onClick={this.navigate.bind(this,"../position/position")}>
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
            <View className="search" onClick={this.navigate.bind(this,'../search/search')}>
              <Image src={searchPng}></Image>
            </View>
        </View>
        <ScrollView scroll-y scroll-top="45" lowerThreshold='30' style='height:100vh' onScrolltolower={this.appendToList.bind(this)} scrollWithAnimation>
          <View className="tabItemContent" hidden={this.state.currentNavtab === 0?false:true}>
            {this.state.onList.map((item,index)=>{
              return (
                <View className="dataItem" key={index} onClick={this.navigateDetail.bind(this,'../detail/detail',item,cityId)}>
                  <View className="leftItem">
                    <Image src={item.poster}></Image>
                  </View>
                  <View className="rightItem">
                    <View className="itemContent">
                      <View className="title">
                        <Text>{item.showName}</Text>
                        <View className="icon">
                          {item.version.split(' ')[0] === "v3d"?<Image src={ico2Png}></Image>:<Image src={ico1Png}></Image>}
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
                      <Image src={item.poster} className="poster"></Image>
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
