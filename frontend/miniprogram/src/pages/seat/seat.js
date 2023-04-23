import Taro, {Component} from '@tarojs/taro'
import {View,Text,MovableView,MovableArea, ScrollView,Image} from '@tarojs/components'
import ableIcon from '../../assets/images/ableIcon.png'
import disableIcon from '../../assets/images/disableIcon.png'
import saleIcon from '../../assets/images/saleIcon.png'
import repairIcon from "../../assets/images/repairIcon.png"
import close from '../../assets/images/close.png'
import jsonData  from '../../assets/data/json.js'
import './seat.scss'


export default class Seat extends Component {
  config = {
    enablePullDownRefresh: false
  }
  constructor(props) {
    super(props);
    this.state = {
      item:{},
      cinemaName:'',
      showId:'',
      movieName: undefined,
      planDetail: undefined,
      seatList: [],
      seatMap:{},
      selectedSeat: [],
      selectedIndex: [],
      hallName: undefined,
      scaleValue: 1,
      hidden: "hidden",
      maxSelect: 4,
      totalPrice: 0,
      loadComplete: false,
      timer: null,
      seatArea: 0,
      rpxToPx: 0,
      maxX:0,
      maxY:0,
      settlePrice:0,
      rowList:[]
    }

  }

  componentDidMount() {

  }

  componentDidShow(){

    const params = this.$router.params;
    this.state.cinemaName = params.cinemaName;
    this.state.showId = params.showId;
    let item = JSON.parse(decodeURIComponent(params.item));
    this.state.item = item;
    this.state.settlePrice = params.price;

    Taro.setNavigationBarTitle({
      title:params.cinemaName
    })

    const res = Taro.getSystemInfoSync();
    console.log(res.screenHeight);
    let seatArea = res.screenHeight - res.statusBarHeight - (500 * res.screenWidth / 750);
    let rpxToPx = res.screenWidth / 750;

    this.state.seatArea =  seatArea;
    this.state.rpxToPx = rpxToPx;

    let token = Taro.getStorageSync("token");

    Taro.showLoading({
      title: '加载中',
    })

    Taro.showLoading({
      title: "加载中..."
    });
    Taro.request({
      url: baseUrl + `/index/seat/${this.state.cinemaName}/${this.state.showId}`,
      method: 'get',
      header: { 'token': token.token }
    }).then(res => {
      if (res.statusCode == 200) {
        Taro.hideLoading();
        let result = jsonData.dataList;
        let data  = res.data;
        let seatList = this.prosessSeatList(data,result.seatTypeList);
        this.setState({
          movieName: item.filmName,
          planDetail: item.showTime,
          hallName: item.hallName,
          seatList: seatList,
          seatTypeList: result.seatTypeList,
          selectedSeat: [],
          totalPrice: 0,
          hidden: "hidden"
        });
        setTimeout(function() {
          wx.hideLoading()
        }, 1000)
        //计算X和Y坐标最大值
        this.prosessMaxSeat(seatList);
        //计算左侧座位栏的数组
        this.seatToolArr();
        //按每排生成座位数组对象
        this.creatSeatMap(seatList);
        //确认最佳坐标座位
        this.creatBestSeat(this.state.maxX,this.state.maxY);


        }else{
          wx.hideLoading()
          wx.showToast({
            title: '获取座位图失败',
            icon: 'none',
            duration: 2000
          })
          setTimeout(function() {
            wx.navigateBack({
              delta: 1, // 回退前 delta(默认为1) 页面
            })
          }, 1000)
        }
    });

    //---这此替换成自己的接口请求成功后--start--


    //---这此替换成自己的接口请求成功后--end-

  }

  //解决官方bug
  handleScale(e) {
    if (this.state.timer) {
      clearTimeout(this.state.timer)
    }
    let timer = setTimeout(() => {
      this.setState({
        seatArea: this.state.seatArea
      });
    }, 200)
  }

  /**
   * 顶级顶部返回按钮时候
   */
  prosessSeatList(resSeatList,seatTypeList) {

    resSeatList.forEach(element => {

      let firstNumber = element.type.substr(0, 1);

      element.otherLoveSeatIndex = null
      element.otherLoveSeatId = null
      // 座位的类型的首字母为 '1' 是情侣首座 处理情侣首座位
      if (firstNumber === '1') {
        for (const index in resSeatList) {
          if (resSeatList[index].gRow === element.gRow &&
            resSeatList[index].gCol === element.gCol + 1) {
            element.otherLoveSeatIndex = index
            element.otherLoveSeatId = resSeatList[index].id
          }
        }
      }
      // 座位的类型的首字母为 '2' 是情侣次座 处理情侣次座位
      if (firstNumber === '2') {
        for (const index in resSeatList) {
          if (resSeatList[index].gRow === element.gRow &&
            resSeatList[index].gCol === element.gCol - 1) {
            element.otherLoveSeatIndex = index
            element.otherLoveSeatId = resSeatList[index].id
          }
        }
      }
      // 加载座位的图标
      let seatType = seatTypeList;
      for (const key in seatType) {
        // 加载每个座位的初始图标defautIcon 和 当前图标 nowIcon
        if (element.type === seatType[key].type) {
          element.nowIcon = seatType[key].icon
          element.defautIcon = seatType[key].icon
        }
        // 根据首字母找到对应的被选中图标
        if (firstNumber + '-1' === seatType[key].type) {
          element.selectedIcon = seatType[key].icon
        }
        // 根据首字母找到对应的被选中图标
        if (firstNumber + '-2' === seatType[key].type) {
          element.soldedIcon = seatType[key].icon
        }
        // 根据首字母找到对应的被选中图标
        if (firstNumber + '-3' === seatType[key].type) {
          element.fixIcon = seatType[key].icon
        }
      }
      // 如果座位是已经售出 和 维修座位 加入属性canClick 判断座位是否可以点击
      if (element.defautIcon === element.soldedIcon || element.defautIcon === element.fixIcon) {
        element.canClick = false
      } else {
        element.canClick = true
      }
    })
    return resSeatList
  }
  //计算最大座位数,生成影厅图大小
  prosessMaxSeat(seatList) {

    let rowList = [];
    let minX = seatList[0].gCol;
    let minY = seatList[0].gRow;
    let maxX = 0;
    let maxY = 0;
    for (let i = 0; i < seatList.length; i++) {
      let tempY = seatList[i].gRow;
      if (parseInt(tempY) > parseInt(maxY)) {
        maxY = tempY;
      }
      if (parseInt(tempY) < parseInt(minY)) {
        minY = tempY;
      }
      var tempX = seatList[i].gCol;
      if (parseInt(tempX) > parseInt(maxX)) {
        maxX = tempX;
      }
      if (parseInt(tempX) < parseInt(minX)) {
        minX = tempX;
      }
      rowList.push(tempY);
    }
    if(minX > 0){
      for (let i = 0; i < seatList.length; i++) {
        seatList[i].gCol = seatList[i].gCol -minX +1;
      }
    }
    if(minY > 0){
      for (let i = 0; i < seatList.length; i++) {
        seatList[i].gRow = seatList[i].gRow -minY +1;
      }
    }

    maxX = maxX-minX +1;
    maxY = maxY-minY +1;

    // 17* 10
    let factor = 170/maxX/maxY;

    let seatRealWidth = parseInt(maxX) * 70 * this.state.rpxToPx * factor;
    let seatRealheight = parseInt(maxY) * 70 * this.state.rpxToPx * factor;
    let seatScale = 1;
    let seatScaleX = 1;
    let seatScaleY = 1;

    let seatAreaWidth = 630 * this.state.rpxToPx * factor;
    let seatAreaHeight = (this.state.seatArea - 200 * this.state.rpxToPx) * factor;
    if (seatRealWidth > seatAreaWidth) {
      seatScaleX = seatAreaWidth / seatRealWidth;
    }
    if (seatRealheight > seatAreaHeight) {
      seatScaleY = seatAreaHeight / seatRealheight;
    }
    if (seatScaleX < 1 || seatScaleY < 1) {
      seatScale = seatScaleX < seatScaleY ? seatScaleX : seatScaleY;
    }

    this.state.maxX = maxX;
    this.state.maxY = maxY;
    this.state.seatScale = seatScale;
    this.state.seatScaleHeight = seatScale * 70 * this.state.rpxToPx;

  }
  // 座位左边栏的数组
  seatToolArr() {
    let seatToolArr = []
    let yMax = this.state.maxY
    let seatList = this.state.seatList
    for (let i = 1; i <= yMax; i++) {
      let el = ''
      for (let j = 0; j < seatList.length; j++) {
        if (parseInt(seatList[j].gRow) === i) {
          el = seatList[j].row
        }
      }
      seatToolArr.push(el)
    }
    this.state.seatToolArr = seatToolArr;
  }
  back() {
    wx.navigateBack({
      delta: 1, // 回退前 delta(默认为1) 页面
    })
  }
  // 点击每个座位触发的函数
  clickSeat(index) {

    if (this.state.seatList[index].canClick) {
      if (this.state.seatList[index].nowIcon === this.state.seatList[index].selectedIcon) {
        this.processSelected(index);
      } else {
        this.processUnSelected(index);
      }
    }
    if (this.state.selectedSeat.length == 0) {
      this.setState({
        hidden: "hidden"
      });
    }

    let _selectedSeatList = this.state.selectedSeat
    let totalPrice = this.state.settlePrice *_selectedSeatList.length;

    this.setState({
      totalPrice: totalPrice.toFixed(2)
    })
  }
  // 处理已选的座位
  processSelected(index) {
    let _selectedSeatList = this.state.selectedSeat
    let seatList = this.state.seatList
    let otherLoveSeatIndex = seatList[index].otherLoveSeatIndex
    if (otherLoveSeatIndex !== null) {
      // 如果是情侣座位
      // 改变这些座位的图标为初始图标
      seatList[index].nowIcon = seatList[index].defautIcon
      seatList[otherLoveSeatIndex].nowIcon = seatList[otherLoveSeatIndex].defautIcon
      for (const key in _selectedSeatList) {
        // 移除id一样的座位
        if (_selectedSeatList[key].id === seatList[index].id) {
          _selectedSeatList.splice(key, 1)
        }
      }
      // 移除对应情侣座位
      for (const key in _selectedSeatList) {
        if (_selectedSeatList[key].id === seatList[otherLoveSeatIndex].id) {
          _selectedSeatList.splice(key, 1)
        }
      }
    } else {
      // 改变这些座位的图标为初始图标 并 移除id一样的座位
      seatList[index].nowIcon = seatList[index].defautIcon
      for (const key in _selectedSeatList) {
        if (_selectedSeatList[key].id === seatList[index].id) {
          _selectedSeatList.splice(key, 1)
        }
      }
    }
    this.setState({
      selectedSeat: _selectedSeatList,
      seatList: seatList
    })
  }
  // 处理未选择的座位
  processUnSelected(index) {
    let _selectedSeatList = this.state.selectedSeat
    let seatList = this.state.seatList
    let otherLoveSeatIndex = seatList[index].otherLoveSeatIndex
    if (otherLoveSeatIndex !== null) {
      // 如果选中的是情侣座位 判断选择个数不大于 maxSelect
      if (_selectedSeatList.length >= this.state.maxSelect - 1) {
        wx.showToast({
          title: '最多只能选择' + this.state.maxSelect + '个座位哦~',
          icon: 'none',
          duration: 2000
        })
        return
      }
      // 改变这些座位的图标为已选择图标
      seatList[index].nowIcon = seatList[index].selectedIcon
      seatList[otherLoveSeatIndex].nowIcon = seatList[otherLoveSeatIndex].selectedIcon
      // 记录 orgIndex属性 是原seatList数组中的下标值
      seatList[index].orgIndex = index
      seatList[otherLoveSeatIndex].orgIndex = otherLoveSeatIndex
      // 把选择的座位放入到已选座位数组中
      let temp = { ...seatList[index]
      }
      let tempLove = { ...seatList[otherLoveSeatIndex]
      }
      _selectedSeatList.push(temp)
      _selectedSeatList.push(tempLove)
    } else {
      // 如果选中的是非情侣座位 判断选择个数不大于 maxSelect
      if (_selectedSeatList.length >= this.state.maxSelect) {
        wx.showToast({
          title: '最多只能选择' + this.state.maxSelect + '个座位哦~',
          icon: 'none',
          duration: 2000
        })
        return
      }
      // 改变这些座位的图标为已选择图标
      seatList[index].nowIcon = seatList[index].selectedIcon
      // 记录 orgIndex属性 是原seatList数组中的下标值
      seatList[index].orgIndex = index
      // 把选择的座位放入到已选座位数组中
      let temp = { ...seatList[index]
      }
      _selectedSeatList.push(temp)
    }
    this.setState({
      selectedSeat: _selectedSeatList,
      seatList: seatList,
      hidden: ""
    })
  }
  confirmHandle() {
    let that = this
    let _this = this.state
    if (_this.selectedSeat.length === 0) {
      wx.showToast({
        title: '请至少选择一个座位~',
        icon: 'none',
        duration: 2000
      })
      return
    }
    // 开始计算是否留下空位 ------------ 开始
    let result = _this.selectedSeat.every(function(element, index, array) {
      return that.checkSeat(element, _this.selectedSeat)
    })
    // 开始计算是否留下空位 ------------ 结束
    if (!result) {
      // 如果 result 为false
      wx.showToast({
        title: '请不要留下空位~',
        icon: 'none',
        duration: 2000
      })
    } else {
      if (_this.totalPrice === 0) {
        wx.showToast({
          title: '锁座失败了~,金额为0',
          icon: 'none',
          duration: 2000
        })
        return
      }
      // 允许锁座
      wx.showLoading({
        title: '加载中',
      })
      that.createOrder()
    }
  }
  // 检查每个座位是否会留下空位
  checkSeat(element, selectedSeat) {
    // 标准为 1.左右侧都必须保留 两格座位 + 最大顺延座位(也就是已选座位减去自身)
    // 2.靠墙和靠已售的座位一律直接通过
    const checkNum = 2 + selectedSeat.length - 1
    const gRowBasic = element.gRow
    const gColBasic = element.gCol
    let otherLoveSeatIndex = element.otherLoveSeatIndex
    if (otherLoveSeatIndex != null) {
      // 如果是情侣座 不检测
      return true
    }
    // 检查座位左侧
    let left = this.checkSeatDirection(gRowBasic, gColBasic, checkNum, '-', selectedSeat)
    // 如果左侧已经检查出是靠着过道直接 返回true
    if (left === 'special') {
      return true
    }
    // 检查座位右侧
    let right = this.checkSeatDirection(gRowBasic, gColBasic, checkNum, '+', selectedSeat)
    if (right === 'special') {
      // 无论左侧是否是什么状态 检查出右侧靠着过道直接 返回true
      return true
    } else if (right === 'normal' && left === 'normal') {
      // 如果左右两侧都有富裕的座位 返回true
      return true
    } else if (right === 'fail' || left === 'fail') {
      // 如果左右两侧都是不通过检测 返回false
      return false
    }
    return true
  }
  // 检查左右侧座位满足规则状态
  checkSeatDirection(gRowBasic, gColBasic, checkNum, direction, selectedSeat) {
    // 空位个数
    let emptySeat = 0
    let x = 1 // 检查位置 只允许在x的位置出现过道,已售,维修
    for (let i = 1; i <= checkNum; i++) {
      let iter // 根据 gRow gCol direction 找出检查座位左边按顺序排列的checkNum
      if (direction === '-') {
        iter = this.state.seatList.find(function(el) {
          return el.gRow === gRowBasic && el.gCol === gColBasic - i
        })
      } else if (direction === '+') {
        iter = this.state.seatList.find(function(el) {
          return el.gRow === gRowBasic && el.gCol === gColBasic + i
        })
      }
      if (x === i) {
        if (iter === undefined) {
          // 过道
          return 'special'
        }
        if (iter.nowIcon === iter.soldedIcon || iter.nowIcon === iter.fixIcon) {
          // 已售或者维修
          return 'special'
        }
        let checkSelect = false
        for (const index in selectedSeat) {
          if (selectedSeat[index].id === iter.id) {
            // 已选 顺延一位
            x++
            checkSelect = true
            break;
          }
        }
        if (checkSelect) {
          continue
        }
      } else {
        if (iter === undefined) {
          // 过道
          return 'fail'
        }
        if (iter.nowIcon === iter.soldedIcon ||
          iter.nowIcon === iter.fixIcon) {
          // 已售或者维修
          return 'fail'
        }
        let checkSelect = false
        for (const index in selectedSeat) {
          if (selectedSeat[index].id === iter.id) {
            return 'fail'
          }
        }
      }
      emptySeat++
      if (emptySeat >= 2) {
        return 'normal'
      }
    }
  }
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {
    return app.globalData.share;
  }
  /**
   * 点击确认选择开始生成订单
   */
  createOrder(url) {

    let _this = this.state
    let selectSeatInfo = _this.selectedSeat;
    let seatInfo = '';
    if (selectSeatInfo) {
      for (var i = 0; i < selectSeatInfo.length; i++) {
        seatInfo = seatInfo +selectSeatInfo[i].seatNo+";";
      }
    }

    let item = this.state.item;
    let price = this.state.settlePrice;
    let buyNum = selectSeatInfo.length;
    let cinemaName = this.state.cinemaName;
    url = url+`?cinemaName=${cinemaName}&buyNum=${buyNum}&price=${price}&seatInfo=${seatInfo}&item=${encodeURIComponent(JSON.stringify(item))}`
    Taro.navigateTo({
      url: url
    });
  }
  //生成最佳座位
  creatBestSeat(maxX,maxY) {
    // 优先左侧
    var bestX = parseInt(maxX / 2) + 1
    // 四舍五入  0.618为黄金分割比例
    var bestY = Math.round(maxY * 0.618)
    this.setState({
      bestX: bestX,
      bestY: bestY,
      loadComplete: true
    })
  }
  // 根据seatList 生成一个类map的对象 key值为gRow坐标 value值为gRow为key值的数组
  creatSeatMap(seatList) {

    var obj = {}
    for (let index in seatList) {
      let seatRowList = seatList[index].gRow
      if (seatRowList in obj) {
        // 原本数组下标
        seatList[index].orgIndex = index
        obj[seatRowList].push(seatList[index])
      } else {
        let seatArr = []
        // 原本数组下标
        seatList[index].orgIndex = index
        seatArr.push(seatList[index])
        obj[seatRowList] = seatArr
      }
    }
    this.setState({
      seatMap: obj
    })
  }
  // 快速选择座位函数
  quickSeat(value) {

    let _self = this.state;
    let that = this;
    // 最优座位数组 里面包含了每排的最佳座位组
    let bestSeatList = [];
    let bestRowSeat;
    for (let i = _self.maxY; i > 0; i--) {
      // bestRowSeat为 gRow 为 i 的的所有座位对象
      bestRowSeat = _self.seatMap[i]
      if (bestRowSeat === undefined) {
        continue
      } else {
        // 找到每排的最佳座位
        let bestSeat = that.seachBestSeatByRow(bestRowSeat, value)
        if (bestSeat != null) {
          bestSeatList.push(bestSeat)
        }
      }
    }
    if (bestSeatList.length <= 0) {
      wx.showToast({
        title: '没有合适的座位~',
        icon: 'none',
        duration: 2000
      })
      return
    }
    let bestSeatListIndex = 0
    // 递归每排的最优座位组 找出离中心点最近的最优座位组
    bestSeatList.reduce(function(prev, cur, index, arr) {
      if (Array.isArray(prev)) {
        // 取中心点离 最好坐标 绝对值
        let n = Math.abs((prev[0].gCol + prev[value - 1].gCol) / 2 - _self.bestX)
        let m = Math.abs(prev[0].gRow - _self.bestY)
        // 勾股定理
        prev = Math.sqrt(Math.pow(n, 2) + Math.pow(m, 2))
      }
      // 取中心点离 最好坐标 绝对值
      let x = Math.abs((cur[0].gCol + cur[value - 1].gCol) / 2 - _self.bestX)
      let y = Math.abs(cur[0].gRow - _self.bestY)
      // 勾股定理
      let z = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))
      if (z >= prev) {
        return prev
      } else {
        bestSeatListIndex = index
        return z
      }
    })
    // 最佳座位中包含情侣座位
    let notEmitSeatArr = []
    // 发送选择事件
    for (const iterator of bestSeatList[bestSeatListIndex]) {
      if (iterator.otherLoveSeatId !== null) {
        let checkFor = false
        for (const item of notEmitSeatArr) {
          if (iterator.id === item) {
            // 情侣座的另外一半不发送事件
            checkFor = true
            break
          }
        }
        if (checkFor) {
          continue
        }
        notEmitSeatArr.push(iterator.otherLoveSeatId)
      }
      that.processUnSelected(iterator.orgIndex)
    }
    let _selectedSeatList = _self.selectedSeat
    let totalPrice = this.state.settlePrice *_selectedSeatList.length;
    this.setState({
      totalPrice: totalPrice.toFixed(2)
    })
  }
  // 找寻每排的最佳座位数组
  seachBestSeatByRow(rowSeatList, value) {
    let effectiveSeatLeft = []
    let effectiveSeatRight = []
    let effectiveSeatMiddle = []
    // 检查居中对齐包含最佳座位的
    effectiveSeatMiddle = this.checkSeatMiddle(rowSeatList, value)
    // 左边检查开始
    effectiveSeatLeft = this.checkSeatWithDirection(rowSeatList, value, '-')
    // 右边检查开始
    effectiveSeatRight = this.checkSeatWithDirection(rowSeatList, value, '+')
    // 如果这排中 包含最佳坐标有座位数满足 返回这批座位数组
    if (effectiveSeatMiddle.length === value) {
      return effectiveSeatMiddle
    }
    // 如果这排中 不包含最佳座位 但是左右两侧都有满足座位数 取离中心点近的方向座位数组
    if (effectiveSeatLeft.length === value && effectiveSeatRight.length === value) {
      return Math.abs(effectiveSeatLeft[0].gCol - this.state.bestX) > Math.abs(effectiveSeatRight[0].gCol - this.state.bestX) ? effectiveSeatRight : effectiveSeatLeft
    } else {
      // 否则 返回 左右两侧 某一侧满足的座位数组
      if (effectiveSeatLeft.length === value) {
        return effectiveSeatLeft
      }
      if (effectiveSeatRight.length === value) {
        return effectiveSeatRight
      }
      return null
    }
  }
  // 找到次排是否有快速选择座位数有效的数组 寻找的坐标为 最佳座位根据快速选择座位数 取左右两边正负座位数
  checkSeatMiddle(rowSeatList, value) {
    let effectiveSeat = []
    let existLoveSeat = false
    // 从负到整的值动态值
    let activeValue = value > 2 ? value - 2 : value - 1
    if (value === this.state.maxX) {
      activeValue = activeValue - 1
    } else if (value > this.state.maxX) {
      // 快速选择座位数 大于影厅横向左边值 直接返回没有有效座位
      return effectiveSeat
    }
    // 最佳座位根据快速选择座位数 取左右两边正负座位数
    for (let j = -activeValue; j <= activeValue; j++) {
      // 确认最佳座位状态
      let iter = rowSeatList.find((el) => (parseInt(el.gCol) === this.state.bestX + j))
      // 最佳座位
      if (iter === undefined) {
        break
      }
      if (iter.nowIcon === iter.soldedIcon || iter.nowIcon === iter.fixIcon) {
        effectiveSeat = []
        existLoveSeat = false
        continue
      } else {
        if (iter.otherLoveSeatId !== null) {
          existLoveSeat = true
        }
        effectiveSeat.push(iter)
      }
    }
    if (effectiveSeat.length > value) {
      // 最后找出居中座位数组后 由于会有已售和维修和过道的影响 在数组中 先删除后面的位置值 再删除前面位置值 直到值为value(快速选择座位数)
      for (let i = 0; i < activeValue; i++) {
        effectiveSeat.pop()
        if (effectiveSeat.length === value) {
          break
        }
        effectiveSeat.shift()
        if (effectiveSeat.length === value) {
          break
        }
      }
      //预检
      if (this.preCheckSeatMakeEmpty(effectiveSeat)) {
        return []
      }
    } else if (effectiveSeat.length < value) {
      return []
    } else {
      //预检
      if (this.preCheckSeatMakeEmpty(effectiveSeat)) {
        return []
      }
    }
    // 如果最近座位组中存在情侣座
    // 检查数组内情侣座必须成对出现 否则舍弃
    if (existLoveSeat) {
      if (!this.checkLoveSeatIsDouble(effectiveSeat)) {
        return []
      }
    }
    return effectiveSeat
  }
  // 找到次排是否有快速选择座位数有效的数组
  checkSeatWithDirection(rowSeatList, value, direction) {
    let activeValue = value
    // 最多允许过道等于3 由于某些影厅 居中的位置不是座位 存在大部分的过道 导致无法选择到最佳座位
    let roadDistance = 3
    let effectiveSeat = []
    let existLoveSeat = false
    for (let j = 0; j < activeValue; j++) {
      let iter
      if (direction === '-') {
        iter = rowSeatList.find((el) => (parseInt(el.gCol) === this.state.bestX - j))
      } else if (direction === '+') {
        iter = rowSeatList.find((el) => (parseInt(el.gCol) === this.state.bestX + j))
      }
      if (iter === undefined) {
        activeValue++
        roadDistance--
        if (roadDistance <= 0) {
          break
        } else {
          continue
        }
      }
      if (iter.nowIcon === iter.soldedIcon || iter.nowIcon === iter.fixIcon) {
        activeValue++
        effectiveSeat = []
        existLoveSeat = false
        continue
      } else {
        if (iter.otherLoveSeatId !== null) {
          existLoveSeat = true
        }
        effectiveSeat.push(iter)
      }
      if (effectiveSeat.length === value) {
        //预检
        if (this.preCheckSeatMakeEmpty(effectiveSeat)) {
          activeValue++
          effectiveSeat.shift()
          continue
        }
      }
    }
    // 如果最近座位组中存在情侣座
    // 检查数组内情侣座必须成对出现 否则舍弃
    if (existLoveSeat) {
      if (!this.checkLoveSeatIsDouble(effectiveSeat)) {
        return []
      }
    }
    return effectiveSeat
  }
  checkLoveSeatIsDouble(arr) {
    // 检查数组内必须情侣座是否对出现 否则舍弃
    var orgSet = new Set()
    var loveSeatSet = new Set()
    for (const iterator of arr) {
      orgSet.add(iterator.id)
    }
    for (const iterator of arr) {
      if (iterator.otherLoveSeatId !== null) {
        loveSeatSet.add(iterator.otherLoveSeatId)
      }
    }
    let beforelen = orgSet.size
    let afterlen = new Set([...orgSet, ...loveSeatSet]).size
    return beforelen === afterlen
  }
  //预检座位
  preCheckSeatMakeEmpty(arr) {
    let that = this
    // 开始计算是否留下空位 ------------ 开始
    let result = arr.every(function(element, index, array) {
      return that.checkSeat(element, arr)
    })
    // 开始计算是否留下空位 ------------ 结束
    return !result
  }



 render() {

    const {seatArea,rpxToPx,seatScaleHeight,maxX,maxY,movieName,seatTypeList ,seatList,selectedSeat } = this.state;

    return (

<View>
    <View className='info'>
      <View className='movieName'>{movieName}</View>
      <View className='planDetail'>{planDetail}</View>
      <View className='about'></View>
    </View>

    <View className="seatDemosBack" v-if={loadComplete}>
     <View className="seatDemos">
      {seatTypeList.map((item, index) => {
          return (<View className="seatDemo" v-if={item.isShow==='1' && item.position==='up'}>
        <Image className="seatDemoItem" mode="widthFix" src={item.icon}></Image>
        <View className="seatDemoItem"> {item.name}</View>
      </View>)})}
    </View>
   </View>

<MovableArea scale-area="true" className="defaultArea" style={{ height: `${seatArea}px`, width: '750rpx' , margin: '20rpx' }}>
  <MovableView className='movableOne' bindscale="handleScale" style={{ height: `${seatArea}px`, width: '750rpx' }} scale="true" direction="all" scale-max="2" scale-min="1" out-of-bounds="true">
    <View className='seatArea' style = {{ width:`${seatScaleHeight * maxX}px`,height:`${seatScaleHeight * maxY}px` }}>
      <View className='alignLine'></View>
      <View className='hallName'>
        <Text>{hallName}</Text>
      </View>
      {seatList.map((item, index) => {
          return (<View className="seatTap" style={{left: `${(item.gCol-1)* seatScaleHeight}px`,top:`${(item.gRow-1) * seatScaleHeight}px`,width: `${seatScaleHeight}px`,height: `${seatScaleHeight}px`}}
          onClick={this.clickSeat.bind(this,index)}>
         <Image src={item.nowIcon} className='normal' />
      </View>)})}
    </View>
  </MovableView>
</MovableArea>

<View className='selectSeatInfo' hidden={hidden}>
  <ScrollView className="scrollSeat" scroll-x style="width: 100%">

   {selectedSeat.map((item, index) => {
       return (
      <View className="scrollItem" onClick ={this.clickSeat.bind(this,item.orgIndex)} >
      <View className='scrollTextTop'>
        {item.seatNo}
      </View>
      <View className='scrollTextBottom'>
        ￥{this.state.settlePrice}
      </View>
      <Image src={close}></Image>
   </View>)})}

  </ScrollView>
</View>


<View className='selectSeatInfo' hidden={!hidden}>
  <ScrollView className="scrollSeat" scroll-x style="width: 100%">
    <View className='quickItem' onClick ={this.quickSeat.bind(this,1)}>
      1人座
    </View>
    <View className='quickItem' onClick ={this.quickSeat.bind(this,2)}>
      2人座
    </View>
    <View className='quickItem' onClick ={this.quickSeat.bind(this,3)} >
      3人座
    </View>
    <View className='quickItem' onClick ={this.quickSeat.bind(this,4)}>
      4人座
    </View>
  </ScrollView>
</View>


<View className='orderComfirm' style="flex-direction:row;">
  <View className='comfirm' onClick={this.createOrder.bind(this, '../order/order')}>￥
    <Text>{totalPrice}</Text> 元 确认选座</View>
</View>

<View hidden>
 <Image src= {ableIcon}/>
 <Image src= {repairIcon}/>
 <Image src= {saleIcon}/>
 <Image src= {disableIcon}/>
</View>


</View>


);
    }
  }
