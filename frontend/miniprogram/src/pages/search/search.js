import Taro, { Component } from '@tarojs/taro'
import { View, Text,Input} from '@tarojs/components'
import './search.scss'
export default class Search extends Component {
  config = {
    navigationBarTitleText: '阿刘电影',
    enablePullDownRefresh:false
  }
  constructor(props){
    super(props);
    this.state={
      keyWord:'',
      cinemasList:[]

    }
  }
  componentDidMount () {
    this.searchList();
  }
  searchList(){
    //let cityId = Taro.getStorageSync('cities').geoCity.id;
    let cityId = 8;
    let keyWord = this.state.keyWord;
    //let keyWord = '万达';
    let self = this;
    if(keyWord != ''){
      Taro.request({
        url:`baseUrl/index/search/${keyWord}/${cityId}`,
        method:'GET'
      }).then(res=>{
        if(res.statusCode == 200){
            let data = res.data.data;
            self.setState({
                cinemasList:data.list
            });
        }
      })
    }

  }
  setKeyWord(e){
    let self = this;
    this.setState({
      keyWord:e.currentTarget.value
    },()=>{
      self.searchList();
    })
  }
  clear(){
    this.setState({
      keyWord:''
    });
    Taro.navigateBack({
      delta:1
    })
  }
  navigateToURL(url,item){
    let cityId = Taro.getStorageSync('cities').geoCity.id;
    url = url+`?title=${item.nm}&id=${item.id}&cityId=${cityId}`
    Taro.navigateTo({
      url:url
    });
  }
  navigateToCinema(url,item){
    console.log(item);
    const cinemaId = item.cinemaId;
    url = url+`?cinemaId=${cinemaId}`
    Taro.navigateTo({
      url:url
    });
  }
  render () {
    let movies = this.state.movieList;
    let cinemas = this.state.cinemasList;
    return (
      <ScrollView className='searchCon' scrollY
        scrollWithAnimation
        scrollTop='0'
        style='height: 100vh;'
        lowerThreshold='20'
      >
        <View className="navHeader">
          <Input className="search" type="text" placeholder="搜影院" onInput={this.setKeyWord.bind(this,e)} value={this.state.keyWord}>
          </Input>
          <View className="cancel" onClick={this.clear.bind(this)}>
              取消
          </View>
        </View>
        <View className="history"></View>
        <View className="resultCon">
          <View className="resultItem" hidden={this.state.cinemasList.length == 0?true:false}>
            <View className="title">影院</View>
            {
              cinemas.map((item,index)=>{
                return (
                  <View className="cinemasItem" key={item.cinemaId} onClick={this.navigateToCinema.bind(this,'../cinemaDetail/cinemaDetail',item)}>
                    <View className="leftCinemas">
                      <View className="cinemaName">{item.cinemaName}</View>
                      <View className="cinemaAddr">{item.address}</View>
                      </View>
                    <View className="cinemasDis">{item.distance}</View>
                  </View>
                )
              })
            }
          </View>
        </View>
      </ScrollView>
    )
  }
}
