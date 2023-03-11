import Taro, { Component } from '@tarojs/taro'
import { View,Text} from '@tarojs/components'
import Brandbar from "../../components/Brandbar/Brandbar";
import Specialbar from "../../components/Specialbar/Specialbar";
import searchPng from "../../assets/images/search2.png";
import './cinema.scss'

export default class Cinema extends Component {
  config = {
    navigationBarTitleText: "影院",
    enablePullDownRefresh:false,
  }
  constructor(props){
    super(props);
    let token = Taro.getStorageSync("token");
    this.state = {
      token:token,
      brand:'123',
      area:'123',
      type:'',
      cityName:'',
      areaData:[],
      brandData:[],
      selectItems:[{nm:'全城',type:'brand'},{nm:'品牌',type:'special'}],
      offset:1,
      cinemas:[]
    }
  }
  selectItem(item){
    if(this.state.type == item.type){
      this.setState({
        type:""
      });
    }else{
      this.setState({
        type:item.type
      });
    }

  }
  getStorageData(){
    let self = this;

    let cityName = Taro.getStorageSync('cityName');
    this.setState({
      cityName:cityName
    },()=>{
      self.filterCinemasList();
      self.getCinemasList();
    });
  }
  filterCinemasList(){
    let cityObj = Taro.getStorageSync('cities');
    Taro.request({
      url:`baseUrl/index/house`,
    }).then(res=>{
      if(res.statusCode == 200){
        this.setState({
          cinemas:res.data.data.list
        });
      }
    })
  }
  getFormatTime(){
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth()+1;
    month = month >=10?'-'+month:'-0'+month;
    let day = date.getDate() >=10?'-'+date.getDate():'-0'+date.getDate();
    return year+month+day;
  }
  getSelectData(){
    Taro.showLoading({
      title:"加载中"
    });
    Taro.request({
      method:'GET',
      url:`baseUrl/index/house`,
    }).then(res=>{
      if(res.statusCode == 200){
        Taro.hideLoading();
        let data = res.data.data;
        this.setState({
          areaData:data.area,
          brandData:data.brand
        });
      }
    })

  }
  getCinemasList(){
    let offset = this.state.offset;
    let area = Taro.getStorageSync('area') == ''?'123':Taro.getStorageSync('area');
    let brand = Taro.getStorageSync('brand') == ''?'123':Taro.getStorageSync('brand');
    let self = this;
    Taro.showLoading({
      title:"加载中"
    });
    Taro.request({
      method:'GET',
      url:`baseUrl/index/cinemaList/`+offset+'/'+area+'/'+brand,
    }).then(res=>{
      if(res.statusCode == 200){
        Taro.hideLoading();
        let data = res.data.data;
        if(data.hasMore > 0){
          if(typeof(self.state.cinemas) == 'undefined'){
            self.state.cinemas = [];
          }
          self.setState({
            cinemas:self.state.cinemas.concat(data.list)
          });
        }
      }
    })
  }
  loadMore(){
    let self = this;
    this.setState({
      offset:self.state.offset+1
    },()=>{
      self.getCinemasList();
    })
  }
  componentDidMount () {
    this.getSelectData();
    this.getStorageData();
  }
  navigate(url){
    Taro.navigateTo({url:url});
  }
  navigateToCinema(url,item){
    let cinemaId = item.cinemaId;
    url = url+`?cinemaId=${cinemaId}`
    Taro.navigateTo({
      url:url
    });
  }
  render () {
    let cinemas = this.state.cinemas;
    return (
      <ScrollView className='cinemas' scrollY
        scrollWithAnimation
        scrollTop='0'
        style='height: 100vh;'
        onScrolltolower={this.loadMore.bind(this)}
        lowerThreshold='20'
      >
        <View className="navHeader">
          <View className="location" onClick={this.navigate.bind(this,'../position/position')}>
            {this.state.cityName}
            <View className="tangle"></View>
          </View>
          <View className="search" onClick={this.navigate.bind(this,'../search/search')}>
            <Image src={searchPng}></Image>
            <Text>搜影院</Text>
          </View>
        </View>
        <View className="ToolBar">
          {this.state.selectItems.map((item,index)=>{
            return (
              <View className={this.state.type == item.type?'actived selectItem':'selectItem'} key={index} onClick={this.selectItem.bind(this,item)}>
                {item.nm}
                <View className="tangle"></View>
              </View>
            )
          })}

          <Specialbar data={this.state.brandData} type={this.state.type}/>
          <Brandbar data={this.state.areaData} type={this.state.type}/>
        </View>
        <View className="cinemasContainer">
        {cinemas.map(item =>{
          return(
            <View className="cinemasItem" key={item.id} onClick={this.navigateToCinema.bind(this,'../cinemaDetail/cinemaDetail',item)}>
              <View className="leftCinemas">
                <View className="cinemaName">{item.cinemaName}<Text className="price">{item.minPrice}</Text><Text className="smallText">元起</Text></View>
                <View className="cinemaAddr">{item.address}</View>
                <View className="cinemaTag"></View>
              </View>
              <View className="cinemasDis">{item.distance}</View>
            </View>
          )})
        }
        </View>
      </ScrollView>
    )
  }
}
