/**
 * Created by Administrator on 2018/8/10 0010.
 */
import Taro, { Component } from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import './order.scss'
export default class Map extends Component {
  constructor(props){
    super(props);
    this.state = {
      cinemaName:'',
      cinemaAddress:'',
      hallName:'',
      showTime:'',
      buyNum:0,
      price:0,
      seatInfo:'',
      item:{},
      price:'0',
      buyNum:0
    }
  }
  config = {
    navigationBarTitleText: '确认订单'
  }
  navigateToUser(url){
    Taro.navigateTo({
      url:url
    })
  }
  initParams() {

debugger
    const params = this.$router.params;
    let item = JSON.parse(decodeURIComponent(params.item));
    this.setState({
      cinemaName:params.cinemaName,
      item:item,
      seatInfo: params.seatInfo,
      price:params.price,
      buyNum:params.buyNum
    })
  }
  createOrder(){

   let token = Taro.getStorageSync("token");
   debugger
   Taro.request({
     url: `baseUrl/order/createOrder`,
     method: 'POST',
     data: {
       cinemaName:this.state.cinemaName,
       cinemaAddress: this.state.cinemaAddress,
       hallName: this.state.hallName,
       showTime: this.state.showTime,
       buyNum: this.state.buyNum,
       price: this.state.price,
       seatInfo: this.state.seatInfo
     },
     header: { 'token': token.token }
   }).then(res => {
     if (res.statusCode == 200) {
       Taro.hideLoading();
       const orderId = res.data.data;
       Taro.request({
         url: `baseUrl/order/payOrder/${orderId}`,
         method: 'get'
       }).then(res => {
         let payParams = res.data.data;
             wx.chooseWXPay({
                 timestamp: payParams.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                 nonceStr: payParams.nonceStr, // 支付签名随机串，不长于 32 位
                 package: payParams.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                 signType: payParams.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                 paySign: payParams.paySign, // 支付签名
             success: function (res) {
             // 支付成功后的回调函数
             }
        });
       });

     }
   })
  }
  componentDidMount () {
    this.initParams();
  }
  render () {
    let showData = this.state.item? this.state.item:{};
    let money = this.state.price * this.state.buyNum;
    let cinemaName = this.state.cinemaName;
    let seatInfo = this.state.seatInfo;
    return (
      <View className="order">
        <View className="timeDown">
          支付剩余时间:
          <View className="restTime">14:00</View>
        </View>
        <View className="movieInfo">
          <View className="movieName">{showData.filmName}</View>
          <View className="movieTime">{showData.showTime}</View>
          <View className="cinemas">{cinemaName} </View>
          <View className="station">{showData.hallName}</View>
          <View className="station">{seatInfo}</View>
        </View>
        <View className="discountInfo">
          <View className="card">
            <View className="name">活动与抵用券</View>
            <View className="orNot">
              <Text className="useful">无可用</Text>
              <Text className="arrow"></Text>
            </View>
          </View>
          <View className="phone">
            <View className="name">手机号码</View>
            <View className="number">
              13415334317
            </View>
          </View>
          <View className="totalMoney">
            <View className="name">小计</View>
            <View className="total">
              ￥{money}
            </View>
          </View>
        </View>
        <View className="line"></View>
        <View className="afford">
          <View className="tickerInfo">
            <View className="info">不支持退票、改签</View>
            <View className="moneyAll">￥{money}</View>
          </View>
          <View className="affordBtn" onClick={this.createOrder.bind(this)}>确认支付</View>
        </View>
      </View>
    )
  }
}
