import Taro, { Component } from "@tarojs/taro";
import { View } from "@tarojs/components";
import { Toptab } from "../../components/Toptab/Toptab.js";
import "./movies.scss";
export default class Movies extends Component {
  config = {
    navigationBarTitleText: "满减电影"
  };
  constructor(props) {
    super(props);
  }
  componentDidMount() {
    //this.autoLogin();
    this.getCities();
  }
  getCities() {
    /* Taro.request({
      url:
        'http://127.0.0.1:8080/index/getCity',
      method: "GET"
    }).then(res => {
      if (res.statusCode == 200) {
        debugger
        let data = res.data;
        Taro.setStorageSync("cityName", data.cityName);
        Taro.setStorageSync("cityCode", data.cityCode);
        Taro.setStorageSync("lat", data.lat);
        Taro.setStorageSync("lon", data.lon);
      }
    }); */
  }

  autoLogin(){
    //微信登录逻辑
    wx.login({
      success (res) {
        if (res.code) {
          //发起网络请求
         Taro.request({
            url: 'baseUrl/wx/maLogin/'+res.code,
            method: 'GET'
          }).then(res=>{
            if(res.statusCode =="200"){
              console.log(res)
              let token = res.data.data;
              Taro.setStorageSync("token",token);
            }
          });
        } else {
          console.log('登录失败！' + res.errMsg)
        }
      }
    })
  }

  render() {
    return (
      <View className="movies">
        <Toptab />
      </View>
    );
  }
}
