import Taro, {
  Component
} from '@tarojs/taro'
import {
  View,
  Image
} from '@tarojs/components'
import Brandbar from "../../components/Brandbar/Brandbar";
import Specialbar from "../../components/Specialbar/Specialbar";
import './detail.scss'

export default class Detail extends Component {
  config = {
    enablePullDownRefresh: false
  }
  constructor(props) {
    super(props);
    this.state = {
      area:'',
      brand:'',
      active: 0,
      params: {},
      offset: 1,
      hide: false,
      type: '',
      flag: true,
      showDate: "",
      detailMovie: {},
      dates: [],
      queryDates: [],
      cinemas: [],
      areaData: [],
      brandData:[],
      selectItems:[{nm:'全城',type:'brand'},{nm:'品牌',type:'special'}],
      scrollLeft: '0',
    }
  }
  componentWillMount() {
    let params = this.$router.params;
    let title = params.title;
    Taro.setNavigationBarTitle({
      title: title
    })
    this.setState({
      params: {
        title: params.title,
        id: params.id,
        cityId: params.cityId
      }
    });
  }

  componentDidMount() {
    this.getDetailData();
    this.getSelectData();
  }
  getfilterCinemas() {
    let cityId = this.state.params.cityId;
    let offset = this.state.offset;
    let area = this.state.area;
    let brand = this.state.brand;
    let token = Taro.getStorageSync("token");
    Taro.request({
      url: `baseUrl/index/schedule/${this.state.params.id}/${this.state.queryDates[this.state.active]}`,
      method: "POST",
      data:{
        offset:offset,
        area:area,
        brand:brand
      },
      header: {'token': token.token}
    }).then(res => {

      if (res.statusCode == 200) {
        let data = res.data.data;
        if (typeof(data.list) != 'undefined') {
          if (this.state.offset < 2) {

            this.state.offset = 2;
            this.setState({
              cinemas: data.list
            });
            this.forceUpdate();
            if(data.hasMore >0){
              this.getfilterCinemas();
            }
          } else {
            data.list.map((item) => {
              this.state.cinemas.push(item);
            })
          }
        } else {
          _index2.default.showToast({
            title: '没有更多数据了',
            icon: 'success',
            duration: 2000
          });
        }
      }
    })
  }
  getSelectData(){
    Taro.showLoading({
      title:"加载中"
    });
    Taro.request({
      method:'GET',
      url:`baseUrl/index/house`,
    }).then(res=>{
      if(res.statusCode == 200){
        Taro.hideLoading();
        let data = res.data.data;
        this.setState({
          areaData:data.area,
          brandData:data.brand
        });
      }
    })

  }

  getDetailData() {

    let token = Taro.getStorageSync("token");
    Taro.request({
      url: `baseUrl/index/movieDetail/${this.state.params.id}`,
      header: {
          "token": token.token
        },
    }).then(res => {
      if (res.statusCode == 200) {
        let data = res.data.data;
        let movie = data.movie;
        let showDate = data.dateList[0];
        this.getHotDate(showDate);
        this.setState({
          detailMovie: movie,
          dates: data.dateList,
          queryDates: data.dateList
        }, () => {
          this.getfilterCinemas()
        });
      }
    }).catch(err => {
      console.log(err.message);
    })
  }
  getDate() {
    let date = new Date();
    let dateString = "";
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    month = month >= 10 ? month : '0' + month
    let day = date.getDate();
    day = day >= 10 ? day : '0' + day;
    dateString = year + "-" + month + "-" + day;
    return dateString;
  }
  getHotDate(showDate) {
    let now = new Date();
    if (new Date(showDate) < now) {
      showDate = this.getDate();
      this.setState({
        showDate: showDate
      });
    }
    Taro.showLoading({
      title: "加载数据中"
    });
    let token = Taro.getStorageSync("token");
    Taro.request({
      url: `baseUrl/index/query`,
      method: 'GET',
      header:{'token':token.token}
    }).then(res => {
      if (res.statusCode == 200) {
        let self = this
        Taro.hideLoading();
        let data = res.data.data;
        let cinemas = this.state.cinemas;
        let showDatas = [];
        this.state.dates.map((item) => {
          let str = item;
          item = this.formatDateString(str);
          showDatas.push(item);
        })
        this.setState({
          dates: showDatas,
          cinemas: self.state.cinemas.concat(cinemas)
        }, () => {});
      }
    })
  }
  formatDateString(date) {
    let dates = new Date();
    let day = dates.getDate();
    let dateParam = date.split('-');
    if (dateParam[2] == day) {
      return "今天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else if (dateParam[2] == day + 1) {
      return "明天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else if (dateParam[2] == day + 2) {
      return "后天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else {
      let weekday = new Date(date).getDay();
      let arr = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"]
      return arr[weekday] + dateParam[1] + "月" + dateParam[2] + "日";
    }
  }
  selectDate(item, index, e) {
    this.setState({
      active: index,
      offset: 1,
      showDate: item.date,
      scrollLeft: e.target.offsetLeft
    }, () => {
      this.getAddrByDay(this.state.queryDates[index])
    });
  }
  getAddrByDay(item) {
    this.state.offset = 1;
    this.getfilterCinemas();
  }
  load(e) {
    let offset = this.state.offset
    offset = offset + 1;
    let showDate = this.state.showDate;
    this.setState({
      offset: offset
    }, () => {
      Taro.showLoading({
        title: '加载中'
      })
      this.getfilterCinemas();
      Taro.hideLoading();
    })
  }
  scroll(e) {
    if (e.detail.scrollTop >= 150 && this.state.flag) {
      let hide = true;
      this.setState({
        hide: hide,
        flag: false
      })
    }
    if (e.detail.scrollTop < 150 && !this.state.flag) {
      let hide = false;
      this.setState({
        hide: hide,
        flag: true
      })
    }
  }
  showArea(name, e) {
    e.stopPropagation();
    e.preventDefault();
    if (this.state.hide) {
      this.setState({
        hide: false,
        type: ''
      });
    } else {
      this.setState({
        hide: true,
        type: name
      });
    }
  }
  selectItem(itemType){
    if(this.state.type == itemType){
      this.setState({
        type:""
      });
    }else{
      this.setState({
        type:itemType
      });
    }
    let area = Taro.getStorageSync('area');
    let brand = Taro.getStorageSync('brand');
    if(area != this.state.area || brand != this.state.brand){
      this.state.area = area;
      let showAreaName = area == '' ? '全城':area;
      this.state.selectItems[0].nm = showAreaName;
      this.state.brand = brand;
      let showBrandName = brand == '' ? '品牌':brand;
      this.state.selectItems[1].nm = showBrandName;
      this.state.offset = 1;
      this.getfilterCinemas();
    }
  }
  restAreaAndBrand(){
    Taro.setStorageSync('area','');
    Taro.setStorageSync('brand','');
    this.state.area = '';
    this.state.brand = '';
    this.state.selectItems[0].nm ='全城';
    this.state.selectItems[1].nm ='品牌';
    this.state.offset = 1;
    this.getfilterCinemas();
  }
  navigateContent(url, item) {
    let cityId = this.state.cityId;
    url = url + `?id=${item.id}&title=${item.nm}&cityId=${cityId}`
    Taro.navigateTo({
      url: url
    })
  }
  navigateToCinema(url, item) {
    url = url + `?cinemaId=${item.cinemaId}&movieId=${this.state.params.id}`;
    Taro.navigateTo({
      url: url
    })
  }
  render() {
    let itemData = this.state.detailMovie;
    let cinemas = this.state.cinemas;
    return ( <
        ScrollView className = "detailContainer"
        scrollY scrollWithAnimation scrollTop = '0'
        style = 'height: 100vh;'
        lowerThreshold = '20'
        upperThreshold = '20'
        onScrolltolower = {
          this.load
        }
        onScroll = {
          this.scroll
        } >
        <
        View className = {
          this.state.hide ? 'hide detailBox' : 'detailBox'
        } >
        <
        View className = "bg" >
        <
        Image src = {
          itemData.backgroundPicture
        } > < /Image> <
        view className = "blurBg" > < /view> <
        View className = "detailContent" >
        <
        Image className = "poster"
        src = {
          itemData.poster
        } > < /Image> <
        View className = "detailInfo" >
        <
        View className = "title" > {
          itemData.showName
        } < /View> <
        View className = "star" > {
          itemData.leadingRole.substring(0, 15)
        } < /View> {
        itemData.globalReleased ? < View className = "comment" > 观众评 {
          itemData.remakr
        } < /View>:<View className="comment">{itemData.wish}人想看</View >
      } <
      View className = "type" > {
        itemData.cat
      } < /View> <
    View className = "hours" > {
      itemData.country
    }
    /{itemData.duration}分钟</View >
    <
    View className = "time" > {
      itemData.pubDesc
    } < /View> < /
    View >  <
      /View> < /
    View > <
      /View> <
    View className = {
        this.state.hide ? 'fix' : ''
      } >
      <
      ScrollView className = 'dateSelect'
    scrollX scrollWithAnimation scrollTop = '0'
    scrollLeft = {
      this.state.scrollLeft
    } > {
      this.state.dates.map((item, index) => {
          return ( < View className = {
              this.state.active == index ? 'active scroll-item' : 'scroll-item'
            }
            key = {
              index
            }
            onClick = {
              this.selectDate.bind(this, item, index)
            } > {
              item
            } < /View>)
          })
      } <
      /ScrollView> <
      View className = "dateSelect" >

      <
      View className = "line" > | < /View> <View className = {
        this.state.type == 'brand' ? 'scroll-item itemActive' : 'scroll-item'
      }
      onClick = {
        this.selectItem.bind(this, 'brand')
      } > {selectItems[0].nm} < View className = "arrow" > < /View></View >
      <View className = "line" > | < /View>
      <View className = {
        this.state.type == 'special' ? 'scroll-item itemActive' : 'scroll-item'
      }
      onClick = {
        this.selectItem.bind(this, 'special')
      } > {selectItems[1].nm} < View className = "arrow" > < /View></View >
      <View className = "line" > | < /View>
      <View className = {
        this.state.type == 'special' ? 'scroll-item itemActive' : 'scroll-item'
      }
      onClick = {
        this.restAreaAndBrand.bind(this)
      } > 重置条件 </View >
      <
      Specialbar data = {
        this.state.brandData
      }
      type = {
        this.state.type
      }
      /> <
      Brandbar data = {
        this.state.areaData
      }
      type = {
        this.state.type
      }
      /> < /
      View > <
      /View> <
      View className = "cinemas" > {
        cinemas.map(item => {
          return ( <
            View className = "cinemasItem"
            key = {
              item.cinemaId
            }
            onClick = {
              this.navigateToCinema.bind(this, "../cinemaDetail/cinemaDetail", item)
            } >
            <
            View className = "leftCinemas" >
            <
            View className = "cinemaName" > {
              item.cinemaName
            } < Text className = "price" > {
              item.minPrice
            } < /Text><Text className="smallText">元起</Text > < /View> <
            View className = "cinemaAddr" > {
              item.address
            } < /View> < /
            View > <
            View className = "cinemasDis" > {
              item.distance
            } < /View> < /
            View >
          )
        })
      } <
      /View> < /
      ScrollView >
    )
  }
}
