import Taro, { Component } from '@tarojs/taro'
import { View,Form,Input,Label} from '@tarojs/components'
import './person.scss'

export default class Person extends Component {
  config = {
    navigationBarTitleText: '阿刘电影',
    enablePullDownRefresh:false,
  }
  constructor(props){
    super(props);
    this.state={
      currentTab:0,
      listItems:["微信一键登录"]
    }
  }
  componentDidMount () { }
  switchTab(index){
    this.setState({
      currentTab:index
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
    this.setState({ isNum: true })
    let { encryptedData, iv } = e.detail
    wx.checkSession({
      success: function () {
        console.log('有session，已登陆');
        //session_key 未过期，并且在本生命周期一直有效
      },
      fail: function () {
        // session_key 已经失效，需要重新执行登录流程
        console.log('未登陆');
        //重新登录
        Taro.login({
          success (res) {
            console.log(res);
            let code = res.code
            Taro.request({
              url: 'url',//后端url
              method: 'GET',
              data: {
                code,//login获取到的code
                encryptedData,
                iv
              },
              success (phoneNumber) {
                console.log(phoneNumber)
              }
            })
          }
        })
      }
    })
  }
  login(url){
    Taro.navigateTo({
      url:url
    })
  }
  render () {
    return (
      <View className='person'>
        <View className="tabCon">
        {this.state.listItems.map((item,index)=>{
            return (
              <View className={this.state.currentTab == index?'tabItem active':'tabItem'} key={index} onClick={this.switchTab.bind(this,index)}>{item}</View>
            )
        })}
        </View>
        <Form onSubmit="formSubmit" onReset="formReset" className="meituan" hidden={this.state.currentTab == 0?false:true}>
            <Button type='primary' openType='getPhoneNumber' onGetPhoneNumber={this.getTel}>微信获取手机号</Button>
        </Form>
        <Form onSubmit="formSubmit" onReset="formReset" className="mobile" hidden={this.state.currentTab == 1?false:true}>
            <Label className='phone' for="phone">
              <Input type="text" placeholder="请输入手机号" id="phone"></Input>
              <Button size="mini">获取验证码</Button>
            </Label>
            <Label className="mobileMail" for="mobileMail">
              <Input type="password" placeholder="请输入短信验证码" id="mobileMail"></Input>
            </Label>
            <Button  className="login" size="default" onClick={this.login.bind(this,'../user/user')}>登陆</Button>
        </Form>
        <View className="copyright">
            <View className="company">© 阿刘电影 客服电话：12345</View>
        </View>
      </View>
    )
  }
}
