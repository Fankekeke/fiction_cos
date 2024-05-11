<template>
  <a-modal v-model="show" title="书籍详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back1" @click="audit(1)" v-if="bookData.status == 0">
        通过
      </a-button>
      <a-button key="back2" @click="audit(2)" type="danger" v-if="bookData.status == 0">
        驳回
      </a-button>
      <a-button key="back" @click="onClose()" type="danger" >
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="bookData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">书籍信息</span></a-col>
        <a-col :span="8"><b>书籍编号：</b>
          {{ bookData.code ? bookData.code : '- -' }}
        </a-col>
        <a-col :span="8"><b>书籍名称：</b>
          {{ bookData.name ? bookData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>最后更新时间：</b>
          {{ bookData.updateDate ? bookData.updateDate : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>标签：</b>
          {{ bookData.tag }}
        </a-col>
        <a-col :span="8"><b>书籍类型：</b>
          <span v-if="bookData.type == 1">玄幻</span>
          <span v-if="bookData.type == 2">奇幻</span>
          <span v-if="bookData.type == 3">武侠</span>
          <span v-if="bookData.type == 4">都市</span>
          <span v-if="bookData.type == 5">现实</span>
        </a-col>
        <a-col :span="8"><b>审核状态：</b>
          <span v-if="bookData.status == 0">审核中</span>
          <span v-if="bookData.status == 1">通过</span>
          <span v-if="bookData.status == 2">驳回</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>最后章节：</b>
          {{ bookData.lastChapter ? bookData.lastChapter : '- -' }}
        </a-col>
        <a-col :span="8"><b>作者名称：</b>
          {{ bookData.authorName ? bookData.authorName : '- -' }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ bookData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>备注：</b>
          {{ bookData.content }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">书籍图片</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
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
  name: 'userView',
  props: {
    bookShow: {
      type: Boolean,
      default: false
    },
    bookData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.bookShow
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
      repairInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null
    }
  },
  watch: {
    bookShow: function (value) {
      if (value) {
        if (this.bookData.images) {
          this.imagesInit(this.bookData.images)
        }
      }
    }
  },
  methods: {
    audit (status) {
      this.$get(`/cos/book-info/audit`, {id: this.bookData.id, status}).then((r) => {
        this.$emit('success')
      })
    },
    local (bookData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(bookData.longitude, bookData.latitude)
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
