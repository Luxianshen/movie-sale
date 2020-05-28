import BaseService from './base-service'

const basePath = 'api/community/message'

export default class MessageService extends BaseService {
  constructor() {
    super()
  }
  async notice() {
    const res = await this.request(basePath + '/notice', null, 'GET')
    if (res.code === 0) {
      return res.data
    }
    return null
  }
  async list(options) {
    const res = await this.request(basePath + '/page', options, 'POST')
    if (res.code === 0) {
      return res.data.map(item => {
        if (item.post && item.post.imgs) {
          item.post.imgs = item.post.imgs.map(img => {
            const path = img.path
            img.path = this.getImgUrl() + path
            return img
          })
        }
        return item
      })
    }
    return null
  }
  async sysList(options) {
    const res = await this.request(basePath + '/sys/list', options, 'POST')
    if (res.code === 0) {
      return res.data
    }
  }
  async read(id, isSys = false) {
    const res = await this.request(basePath + '/read', { id, isSys }, 'POST')
    if (res.code === 0) {
      return true
    }
    return false
  }
}
