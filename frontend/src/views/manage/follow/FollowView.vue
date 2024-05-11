<template>
  <a-modal v-model="show" title="用户关注详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="recordData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>用户编号：</b>
          {{ recordData.userCode ? recordData.userCode : '- -' }}
        </a-col>
        <a-col :span="8"><b>用户名称：</b>
          {{ recordData.userName ? recordData.userName : '- -' }}
        </a-col>
        <a-col :span="8"><b>关注时间：</b>
          {{ recordData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <div style="font-size: 13px;font-family: SimHei" v-if="authorInfo !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">创作者信息</span></a-col>
          <a-col :span="8"><b>创作者编号：</b>
            {{ authorInfo.code ? authorInfo.code : '- -' }}
          </a-col>
          <a-col :span="8"><b>创作者姓名：</b>
            {{ authorInfo.name ? authorInfo.name : '- -' }}
          </a-col>
          <a-col :span="8"><b>联系方式：</b>
            {{ authorInfo.phone }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>创建时间：</b>
            {{ authorInfo.createDate }}
          </a-col>
          <a-col :span="8"><b>性别：</b>
            <span v-if="authorInfo.sex == 1">男</span>
            <span v-if="authorInfo.sex == 2">女</span>
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="24"><b>备注：</b>
            {{ authorInfo.content }}
          </a-col>
        </a-row>
        <br/>
        <br/>
      </div>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'recordView',
  props: {
    recordShow: {
      type: Boolean,
      default: false
    },
    recordData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.recordShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      authorInfo: null,
      recordInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null
    }
  },
  watch: {
    recordShow: function (value) {
      if (value) {
        this.selectDetail(this.recordData.authorId)
      }
    }
  },
  methods: {
    selectDetail (id) {
      this.$get(`/cos/author-info/${id}`).then((r) => {
        this.authorInfo = r.data.data
      })
    },
    local (recordData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(recordData.longitude, recordData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
