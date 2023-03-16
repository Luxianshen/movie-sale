import Taro, { Component } from '@tarojs/taro'
import { View,Text, Button} from '@tarojs/components'
import './orderList.scss'

export default class OrderList extends Component {
  config = {
    enablePullDownRefresh:false
  }
  constructor(props){
    super(props);
    let token = Taro.getStorageSync("token");
    this.state = {
      token:token,
      brand:'',
      area:'',
      type:'',
      cityName:'',
      areaData:[],
      brandData:[],
      selectItems:[{nm:'全城',type:'brand'},{nm:'品牌',type:'special'}],
      offset:1,
      orders:[]
    }
  }

  getStorageData(){
    this.getOrderList();
  }

  getOrderList(){
    let offset = this.state.offset;
    let token = Taro.getStorageSync("token");
    Taro.showLoading({
      title:"加载中"
    });
    Taro.request({
      method:'GET',
      url:`baseUrl/cz/order/myOrderPage/`+offset,
      header:{'token':token.token}
    }).then(res=>{
      if(res.statusCode == 200){
        Taro.hideLoading();
        let self = this;
        let data = res.data.data.records;
        if(typeof(data) != 'undefined'){
          if(typeof(self.state.orders) == 'undefined'){
            self.state.orders = [];
          }
          if(this.state.offset == 1){
            self.state.orders = [];
            this.forceUpdate();
          }
          self.setState({
            orders:self.state.orders.concat(data)
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
      self.getOrderList();
    })
  }
  componentDidMount () {
    this.getStorageData();
  }
  navigate(url){
    Taro.navigateTo({url:url});
  }

  navigateToOrderDetail(url,item){
    
    Taro.navigateTo({
      url:url
    });
  }

  render () {

    let orders = this.state.orders ? this.state.orders :[];
    return (
      <ScrollView className='orders' scrollY
        scrollWithAnimation
        scrollTop='0'
        style='height: 100vh;'
        onScrolltolower={this.loadMore.bind(this)}
        lowerThreshold='20'
      >

        <View className="orderContainer">
        {orders.map(item =>{
          return(
            <View className="orderItem" key={item.id} onClick={this.navigateToOrderDetail.bind(this,'../qrcode/qdcode',item)}>
              <View className="leftOrders">
                <View className="orderName">{item.cinemaName}<Text className="price">厅号:{item.hallName}</Text>
                <Text className="smallText">位置:{item.seatInfo}</Text></View>
                <View className="orderAddr">开场时间:{item.showTime}</View>
                <View className="orderTag">
                 <Text>出票中...</Text>
                 <Button>兑票码</Button> <Button>支付订单</Button>
                </View>
              </View>
            </View>
          )})
        }
        </View>
      </ScrollView>
    )
  }
}
