import Taro, { Component } from '@tarojs/taro'
import { View,Text, Button} from '@tarojs/components'
import './orderList.scss'
import Nothing from "../../components/Nothing/Nothing";

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
      orders:[],
      loadFlag: true
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
          if(this.state.offset != 1 &&data.length == 0){
            Taro.showToast({
              title: '没有更多数据了',
              icon: 'success',
              duration: 2000
            });
            self.setState({
              loadFlag:false
            });
          }
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
    if(this.state.loadFlag){
      let self = this;
      this.setState({
        offset:self.state.offset+1
      },()=>{
        self.getOrderList();
      })
    }
  }
  componentDidMount () {
    this.getStorageData();
  }
  payOrder(orderId){
    let token = Taro.getStorageSync('token');
    Taro.request({
      url: `baseUrl/order/payOrder/${orderId}`,
      method: 'POST',
      header: { 'token': token.token }
    }).then(res => {
      let payParams = res.data.data;
          wx.chooseWXPay({
              timestamp: payParams.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
              nonceStr: payParams.nonceStr, // 支付签名随机串，不长于 32 位
              package: payParams.prepayId, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
              signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
              paySign: payParams.sign, // 支付签名
          success: function (res) {
          // 支付成功后的回调函数
          }
     });
    });
  }
  showQrCode(img){
    let a = img;
    Taro.downloadFile({
      url: img,
      success: (res) => {
        wx.showShareImageMenu({
          path: res.tempFilePath
        })
      }
    })
  }

  navigateToOrderDetail(url,item){
    Taro.navigateTo({
      url:url
    });
  }

  warnOrder(){
    Taro.showModal({
      content: '订单出票需10-50分，请耐心等待',
      showCancel: false
    });
  }

  closeOrder(orderId){
    let self = this;
    let token = Taro.getStorageSync('token');
    Taro.request({
      url: `baseUrl/order/closeOrder/${orderId}`,
      method: 'DELETE',
      header: { 'token': token.token }
    }).then(res => {
         if(res.statusCode == '200'){
           Taro.showToast({
             title: '订单删除成功！',
             icon: 'success',
             duration: 2000
           });
           this.forceUpdate();
         }
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
     <View className="things">
      <View className="title">订单列表</View>
     </View>

        <View class='container order-list-page' hidden={orders.length==0?true:false}>
        {orders.map(item =>{
          return(
          <View class='order-item'>
            <Navigator class='order-title line-ellipsis'  hover-class='none'>
              <View>{item.cinemaName}</View>
              <View class='triangle'></View>
            </Navigator>
            <View class='order-info' >
              <Image src={item.movieImg}></Image>
              <View class='order-desc'>
                <View class='delete-box' catchtap='deleteOrder'>
                  <View class='iconfont icon-del delete'></View>
                </View>
                <View class='movie-name line-ellipsis'>{item.movieName} {item.buyNum}张</View>
                <View class='showTime line-ellipsis'>开场时间: {item.showTime}</View>
                <View class='position line-ellipsis'>放映厅号: {item.hallName}</View>
                <View class='position line-ellipsis'>座位信息: {item.seatInfo}</View>
              </View>
            </View>
            <View class='order-more'>
              <View className="money">总价：{item.actualAmount}元</View>
              <View>
               <View className="waitCode" hidden={item.orderState == 2?false:true}  onClick={this.warnOrder.bind(this)} >出票中</View>
                <View className="closeOrder" hidden={item.orderState >=0 && item.orderState < 2?false:true} onClick={this.closeOrder.bind(this,item.id)} >关闭订单</View>
                <View className="buyTicket" hidden={item.orderState == 1?false:true} onClick={this.payOrder.bind(this,item.id)} >支付订单</View>
                <View className="preBuy" hidden={item.orderState == 3 ? false : true} onClick={this.showQrCode.bind(this,item.ticketPic)}>兑票码</View>
              </View>
              </View>
          </View>

          )})
          }
        </View>

      <Nothing data = '暂无记录呢' hideFlag = {orders.length == 0? false:true} />
      </ScrollView>
    )
  }
}
