<template>
  <a-modal v-model="show" title="教师请假详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back1" @click="onAudit(1)">
        通过
      </a-button>
      <a-button key="back" @click="onAudit(2)" type="danger">
        驳回
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="memberData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>教师编号：</b>
          {{ memberData.teacherCode ? memberData.teacherCode : '- -' }}
        </a-col>
        <a-col :span="8"><b>教师姓名：</b>
          {{ memberData.teacherName ? memberData.teacherName : '- -' }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ memberData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>创建时间：</b>
          {{ memberData.createDate }}
        </a-col>
        <a-col :span="8"><b>请假天数：</b>
          {{ memberData.days }} 天
        </a-col>
        <a-col :span="8"><b>审批状态：</b>
          <span v-if="memberData.status == 0">未审批</span>
          <span v-if="memberData.status == 1">通过</span>
          <span v-if="memberData.status == 2">驳回</span>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">请加内容</span></a-col>
        <a-col :span="24">
          {{ memberData.auditTitle ? memberData.auditTitle : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">图片</span></a-col>
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
  name: 'memberView',
  props: {
    memberShow: {
      type: Boolean,
      default: false
    },
    memberData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.memberShow
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
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null
    }
  },
  watch: {
    memberShow: function (value) {
      if (value) {
        if (this.memberData.images) {
          this.imagesInit(this.memberData.images)
        }
      }
    }
  },
  methods: {
    onAudit (status) {
      this.$get(`/cos/leave-teacher-info/leaveAudit`, {
        id: this.memberData.id,
        status: status
      }).then((r) => {
        this.$emit('success')
      })
    },
    local (memberData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(memberData.longitude, memberData.latitude)
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
