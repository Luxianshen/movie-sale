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
      cinemasList:[],
      hidden: true

    }
  }
  componentDidMount () {
    this.searchList(this);
  }
  searchList(){

    let keyWord = this.state.keyWord;
    //let keyWord = '万达';
    let token  = Taro.getStorageSync('token');

    if(keyWord != ''){
      Taro.request({
        url:baseUrl + `/index/search/${keyWord}`,
        method:'GET',
        header:{'token':token.token}
      }).then(res=>{
        if(res.statusCode == 200){
            let data = res.data.data;
            this.state.cinemasList =data.list;
            this.state.hidden = data.list.length == 0;
            this.forceUpdate();
        }
      })
    }

  }

  handleInputChange(event) {
      this.setState({
        keyWord: event.target.value,
      },()=>{
      this.searchList();
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
    const { keyWord,cinemasList,hidden}  = this.state;

    return (
      <ScrollView className='searchCon' scrollY
        scrollWithAnimation
        scrollTop='0'
        style='height: 100vh;'
        lowerThreshold='20'
      >
        <View className="navHeader">
          <Input className="search" type="text" placeholder="搜影院" onInput={this.handleInputChange.bind(this)} value={keyWord}>
          </Input>
          <View className="cancel" onClick={this.clear.bind(this)}>
              取消
          </View>
        </View>
        <View className="history"></View>
        <View className="resultCon">
          <View className="resultItem" >
            <View className="title">影院</View>
            {
              cinemasList.map((item,index)=>{
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
