import Taro, { Component } from '@tarojs/taro'
import { View,Text} from '@tarojs/components'
import { Brandbar } from "../../components/Brandbar/Brandbar";
import { Areabar } from "../../components/Areabar/Areabar";
import searchPng from "../../assets/images/search2.png";
import './cinema.scss'

export default class Cinema extends Component {
  config = {
    navigationBarTitleText: "影院",
    enablePullDownRefresh:false,
  }
  constructor(props){
    super(props);
    this.state = {
      brand:'',
      nowArea:'',
      type:'',
      cityName:'',
      areaData:[],
      brandData:[],
      selectItems:[{nm:'全城',type:'area'},{nm:'品牌',type:'brand'}],
      offset:1,
      cinemas:[]
    }
  }
  selectItem(itemType){
    if(this.state.type == itemType || itemType == ''){
      this.setState({
        type:""
      });
    }else{
      this.setState({
        type:itemType
      });
    }
    let nowArea = Taro.getStorageSync('nowArea');
    let brand = Taro.getStorageSync('brand');
    if(nowArea != this.state.nowArea || brand != this.state.brand){
      this.state.nowArea = nowArea;
      let showAreaName = nowArea == '' ? '全城':nowArea;
      this.state.selectItems[0].nm = showAreaName;
      this.state.brand = brand;
      let showBrandName = brand == '' ? '品牌':brand;
      this.state.selectItems[1].nm = showBrandName;
      this.state.offset = 1;
      this.getCinemasList();
    }
  }
  restAreaAndBrand(){
    Taro.setStorageSync('nowArea','');
    Taro.setStorageSync('brand','');
    this.state.nowArea = '';
    this.state.brand = '';
	this.selectItem('');
    this.state.selectItems[0].nm = '全城';
    this.state.selectItems[1].nm = '品牌';
    this.state.offset = 1;
    this.getCinemasList();
  }
  getStorageData(){
    let self = this;

    let cityName = Taro.getStorageSync('cityName');
    this.setState({
      cityName:cityName
    },()=>{
      self.getCinemasList();
    });
  }
  filterCinemasList(){
    Taro.request({
      url:baseUrl + `/index/house`,
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
      url:baseUrl + `/index/house`,
    }).then(res=>{
      if(res.statusCode == 200){
        Taro.hideLoading();
        let data = res.data.data;
        Taro.setStorageSync("cityId",data.area[0].cityId);
        this.setState({
          areaData:data.area,
          brandData:data.brand
        });
      }
    })

  }
  getCinemasList(){
    let offset = this.state.offset;
    let nowArea = this.state.nowArea;
    let brand = this.state.brand;
    let self = this;
    let token = Taro.getStorageSync("token");
    Taro.showLoading({
      title:"加载中"
    });
    Taro.request({
      method:'POST',
      url:baseUrl + `/index/cinemaList`,
      data:{
        offset: offset,
        area: nowArea,
        brand:brand
      },
      header:{'token':token.token}
    }).then(res=>{
      if(res.statusCode == 200){

        let data = res.data.data;
        if(typeof(data.list) != 'undefined'){
          if(typeof(self.state.cinemas) == 'undefined'){
            self.state.cinemas = [];
          }
          if(this.state.offset == 1){
            self.state.cinemas = [];
            this.forceUpdate();
          }
          self.setState({
            cinemas:self.state.cinemas.concat(data.list)
          });
        }
        Taro.hideLoading();
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
  navigatePostion(url){
    Taro.showToast({
      title: '暂不支持选择',
      icon: 'success',
      duration: 2000
    });
    return false;
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
    const { cityName,areaData,brandData,cinemas,selectItems,type } = this.state;

    return (
      <ScrollView className='cinemas' scrollY
        scrollWithAnimation
        scrollTop='0'
        style='height: 100vh;'
        onScrolltolower={this.loadMore.bind(this)}
        lowerThreshold='20'
      enable-flex>
        <View className="navHeader">
          <View className="location" onClick={this.navigatePostion.bind(this,'../position/position')}>
            {cityName}
            <View className="tangle"></View>
          </View>
          <View className="search" onClick={this.navigate.bind(this,'../search/search')}>
            <Image src={searchPng}></Image>
            <Text>搜影院</Text>
          </View>
        </View>
        <View className="ToolBar">
          {selectItems.map((item,index)=>{
            return (
              <View className={this.state.type == item.type?'actived selectItem':'selectItem'} key={index} onClick={this.selectItem.bind(this,item.type)}>
                {item.nm}
                <View className="tangle"></View>
              </View>
            )
          })}
          <View className='selectItem' onClick={this.restAreaAndBrand.bind(this)}>
            重置条件
          </View>

          <Areabar data={areaData} showFlag={type == 'area'} onClick={(arg) => this.selectItem(arg)} />
          <Brandbar data={brandData} showFlag={type =='brand'} onClick={(arg) => this.selectItem(arg)} />

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
