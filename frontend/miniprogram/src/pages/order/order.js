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

    this.setState({
      cinemaName:params.cinemaName,
      item:item,
      price:params.price,
      buyNum:params.buyNum
    })
  }
  componentDidMount () {
    this.initParams();
  }
  render () {
    let showData = this.state.item? this.state.item:{};
    let money = Math.floor(this.state.price) * this.state.buyNum;
    let cinemaName = this.state.cinemaName;
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
          <View className="affordBtn" onClick={this.navigateToUser.bind(this,'../user/user')}>确认支付</View>
        </View>
      </View>
    )
  }
}
