/**
 * Created by Administrator on 2018/8/10 0010.
 */
import Taro, { Component } from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import './order.scss'
export default class Order extends Component {
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

    const params = this.$router.params;
    let item = JSON.parse(decodeURIComponent(params.item));
    // 20-4 30-5 40-6 50-7
    this.setState({
      cinemaName:params.cinemaName,
      item:item,
      seatInfo: params.seatInfo,
      price: params.price,
      buyNum:params.buyNum
    })
  }
  createOrder(){

    let token = Taro.getStorageSync("token");
   Taro.request({
     url: baseUrl + `/order/createOrder`,
     method: 'POST',
     data: {
       cinemaName:this.state.cinemaName,
       cinemaAddress: this.state.cinemaAddress,
       hallName: this.state.item.hallName,
       showTime: this.state.item.showTime,
       buyNum: this.state.buyNum,
       price: this.state.price,
       seatInfo: this.state.seatInfo,
       movieName: this.state.item.filmName,
       movieImg: this.state.item.bg
     },
     header: { 'token': token.token }
   }).then(res => {
     if (res.statusCode == 200) {
       Taro.hideLoading();
       const orderId = res.data.data;
       Taro.request({
         url: baseUrl + `/order/payOrder/${orderId}`,
         method: 'POST',
         header: { 'token': token.token }
       }).then(res => {
         debugger
         Taro.redirectTo({
           url:'../orderList/orderList'
         })
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
   })
  }

  componentDidMount () {
    this.initParams();
  }
  render () {
    const {item,price,buyNum,cinemaName,seatInfo} = this.state;

    let token = Taro.getStorageSync("token");

    return (
      <View className="order">
        <View className="timeDown">
          请在15分钟内完成支付
          <View className="restTime"></View>
        </View>

       <View className="detailBox">
         <View className ="bg">
           <View className="blurBg"></View>
           <View className="detailContent">
             <Image className="poster" src={item.bg}></Image>
             <View className="detailInfo">
             <View className="title">影院: {cinemaName}</View>
               <View className="title">电影: {item.filmName}</View>
               <View className="title">厅名: {item.hallName}</View>
               <View className="title">座位: {seatInfo}</View>
               <View className="time">时间: {item.showTime}</View>
             </View>
           </View>
         </View>
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
              {token.phone}
            </View>
          </View>
          <View className="totalMoney">
            <View className="name">小计</View>
            <View className="total">
              ￥{price*buyNum}
            </View>
          </View>
        </View>
        <View className="line"></View>
        <View className="afford">
          <View className="tickerInfo">
            <View className="info">不支持退票、改签</View>
            <View className="moneyAll">￥{price*buyNum}</View>
          </View>

        </View>
        <View className='orderComfirm' style="flex-direction:row;">
           <View className="affordBtn" onClick={this.createOrder.bind(this)}>确认支付</View>
        </View>
      </View>
    )
  }
}
