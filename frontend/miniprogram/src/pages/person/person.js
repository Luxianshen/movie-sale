import Taro, {
  Component
} from '@tarojs/taro'
import {
  View,
  Form,
  Input,
  Label
} from '@tarojs/components'
import './person.scss'
import './user.scss';
import avatarPng from "../../assets/images/avatar.png"
import moviePng from "../../assets/images/movie.png"
import minePng from "../../assets/images/mine.png"

export default class Person extends Component {
  config = {
    navigationBarTitleText: '阿刘电影',
    enablePullDownRefresh: false,
  }
  constructor(props) {
    super(props);
    this.state = {
      currentTab: 0,
      listItems: ["微信一键登录"]
    }
  }

  componentDidMount() {
    let token = Taro.getStorageSync("token");
    if (typeof(token.phone) != 'undefined') {
      this.state.currentTab ==1;
      this.switchTab(1);
    }
  }
  switchTab(index) {
    this.setState({
      currentTab: index
    });
  }
  formSubmit = e => {
    console.log(e)
  }

  formReset = e => {
    console.log(e)
  }
  getTel = (e) => {
    console.log(e.detail);
    this.setState({
      isNum: true
    })
    let {
      encryptedData,
      iv
    } = e.detail;

    if (e.detail.errMsg == "getPhoneNumber:ok") {
      let code = e.detail.code;
      let encryptedData = e.detail.encryptedData;
      let iv = e.detail.iv;

      let token = Taro.getStorageSync("token");
      let sessionKey = token.sessionKey;

      Taro.request({
        url: 'baseUrl/wx/maBindPhone', //后端url
        method: 'POST',
        header: {
          'token': token.token
        },
        data: {
          code: code,
          encryptedData: encryptedData,
          iv: iv,
          sessionKey: sessionKey
        }
      }).then(res => {
        if (res.statusCode == '200') {
          Taro.setStorageSync("token", res.data.data.token);
          console.log(token);
          this.state.currentTab = 1;
          this.switchTab(1);
        }
      })
    } else {

    }

  }
  login() {
    Taro.navigateTo({
      url: '../user/user'
    })
  }
  render() {
    return (
    <view>
    <View className="userCenter" hidden = {
        this.state.currentTab == 1 ? false : true
      }>
      <View className="userInfo">
        <View className="avatar">
          <Image src={avatarPng}></Image>
        </View>
        <View className="name">阿刘</View>
      </View>
      <View className="order">
        <View className="myOrder">
          <View className="line"></View>
          <View className="tip">我的订单</View>
        </View>
        <View className="list">
          <View className="movie">
            <Image src={moviePng}></Image>
            <Text className="item">电影</Text>
          </View>
          <View className="shop">
            <Image src={minePng}></Image>
            <Text className="item">商城</Text>
          </View>
        </View>
        <View className="discount">
          <View className="vipCard">
            <View className="desc">优惠券</View>
            <View className="arrow">></View>
          </View>
          <View className="cardpon">
            <View className="desc">折扣卡</View>
            <View className="arrow">></View>
          </View>
        </View>
      </View>
    </View>

    <View className = 'person' hidden = {
        this.state.currentTab == 0 ? false : true
      }>
      <
      View className = "tabCon" > {
        this.state.listItems.map((item, index) => {
          return ( <
            View className = {
              this.state.currentTab == index ? 'tabItem active' : 'tabItem'
            }
            key = {
              index
            }
            onClick = {
              this.switchTab.bind(this, index)
            } > {
              item
            } < /View>
          )
        })
      } <
      /View> <
      Form onSubmit = "formSubmit"
      onReset = "formReset"
      className = "meituan"
       >
      <
      Button type = 'primary'
      openType = 'getPhoneNumber'
      onGetPhoneNumber = {
        this.getTel
      } > 微信获取手机号 < /Button> < /
      Form >

       <
      View className = "copyright" >
      <
      View className = "company" > ©阿刘电影 客服电话： 12345 < /View> < /
      View > <
      /View>
 </view>

    )
  }
}
