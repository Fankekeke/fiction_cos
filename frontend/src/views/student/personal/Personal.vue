<template>
  <a-row :gutter="20">
    <a-col :span="8">
      <a-card :loading="loading" :bordered="false">
        <a-form :form="form" layout="vertical">
          <a-row :gutter="20">
            <a-col :span="12">
              <a-form-item label='学生编号' v-bind="formItemLayout">
                <a-input disabled v-decorator="[
                'code',
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='注册时间' v-bind="formItemLayout">
                <a-input disabled v-decorator="[
                'createDate',
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='学生姓名' v-bind="formItemLayout">
                <a-input v-decorator="[
                'name',
                { rules: [{ required: true, message: '请输入用学生姓名!' }] }
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='出生日期' v-bind="formItemLayout">
                <a-date-picker style="width: 100%;" v-decorator="[
                'birthday',
                { rules: [{ required: true, message: '请输入出生日期!' }] }
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='联系方式' v-bind="formItemLayout">
                <a-input v-decorator="[
                'phone'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='性别' v-bind="formItemLayout">
                <a-select v-decorator="[
                  'sex',
                  ]">
                  <a-select-option value="1">男</a-select-option>
                  <a-select-option value="2">女</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label='学生兴趣' v-bind="formItemLayout">
                <a-textarea :rows="4" v-decorator="[
                'interest'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label='备注' v-bind="formItemLayout">
                <a-textarea :rows="4" v-decorator="[
                'content',
                 { rules: [{ required: true, message: '请输入备注!' }] }
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label='头像' v-bind="formItemLayout">
                <a-upload
                  name="avatar"
                  action="http://127.0.0.1:9527/file/fileUpload/"
                  list-type="picture-card"
                  :file-list="fileList"
                  @preview="handlePreview"
                  @change="picHandleChange"
                >
                  <div v-if="fileList.length < 1">
                    <a-icon type="plus" />
                    <div class="ant-upload-text">
                      Upload
                    </div>
                  </div>
                </a-upload>
                <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                  <img alt="example" style="width: 100%" :src="previewImage" />
                </a-modal>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
          修改
        </a-button>
      </a-card>
    </a-col>
    <a-col :span="12" v-if="expertInfo != null" style="margin-top: 30px;padding-left: 50px">
      <a-row>
        <a-col :span="12">
          <div style="font-size: 20px;font-family: SimHei;margin-left: 30px">我的信息</div>
          <a-card :bordered="false">
            <a-row>
              <a-col :span="12">
                <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + expertInfo.images" shape="square" style="width: 100px;height: 100px;"/>
              </a-col>
              <a-col :span="12">
                <div style="font-size: 20px;font-family: SimHei">{{ expertInfo.name }}</div>
                <p style="font-size: 13px;font-family: SimHei">{{ expertInfo.code }}</p>
              </a-col>
            </a-row>
          </a-card>
        </a-col>
        <a-col :span="12">
          <div style="font-size: 20px;font-family: SimHei;margin-left: 30px">我的导师</div>
          <a-card :bordered="false" v-if="teacherInfo != null">
            <a-row>
              <a-col :span="12">
                <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + teacherInfo.images" shape="square" style="width: 100px;height: 100px;"/>
              </a-col>
              <a-col :span="12">
                <div style="font-size: 20px;font-family: SimHei">{{ teacherInfo.name }}</div>
                <p style="font-size: 13px;font-family: SimHei">{{ teacherInfo.code }}</p>
                <p style="font-size: 13px;font-family: SimHei">联系方式：{{ teacherInfo.phone }}</p>
              </a-col>
            </a-row>
          </a-card>
        </a-col>
      </a-row>
      <div style="font-size: 20px;font-family: SimHei;margin-left: 30px;margin-top: 30px">推荐课程</div>
      <a-list item-layout="horizontal" :data-source="coursesList" style="margin-top: 10px;width: 100%">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-list-item-meta
            :description="item.content"
          >
            <a slot="title">
              <span>{{ item.courseName }}</span>
              <span> - {{ item.teacherName }}</span>
            </a>
            <a-avatar
              slot="avatar"
              :src="'http://127.0.0.1:9527/imagesWeb/' + item.images.split(',')[0]"
            />
          </a-list-item-meta>
        </a-list-item>
      </a-list>
    </a-col>
  </a-row>
</template>

<script>
import {mapState} from 'vuex'
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
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'User',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      form: this.$form.createForm(this),
      formItemLayout,
      loading: false,
      courseInfo: [],
      dataLoading: false,
      fileList: [],
      coursesList: [],
      previewVisible: false,
      previewImage: '',
      expertInfo: null,
      teacherInfo: null
    }
  },
  mounted () {
    this.getExpertInfo(this.currentUser.userId)
    this.recommendCourses(this.currentUser.userId)
    this.getTeacher(this.currentUser.userId)
  },
  methods: {
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr)
      let beginDate = new Date(beginDateStr)
      let endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData (value) {
      let listData = []
      this.courseInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.payDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: '✔'})
        }
      })
      return listData || []
    },
    recommendCourses (userId) {
      this.$get(`/cos/savor-info/recommendCourses/${userId}`).then((r) => {
        this.coursesList = r.data.data
      })
    },
    getTeacher (userId) {
      this.$get(`/cos/student-info/teacher/${userId}`).then((r) => {
        this.teacherInfo = r.data.data
      })
    },
    getExpertInfo (userId) {
      this.dataLoading = true
      this.$get(`/cos/student-info/detail/${userId}`).then((r) => {
        this.expertInfo = r.data.data
        console.log(this.expertInfo)
        this.setFormValues(this.expertInfo)
        // this.courseInfo = r.data.order
        this.dataLoading = false
      })
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...expert}) {
      this.rowId = expert.id
      let fields = ['code', 'name', 'phone', 'sex', 'email', 'images', 'createDate', 'birthday', 'content', 'interest']
      let obj = {}
      Object.keys(expert).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(expert['images'])
        }
        if (key === 'birthday') {
          if (key === 'birthday' && expert[key] != null) {
            expert[key] = moment(expert[key])
          }
        }
        if (key === 'sex') {
          expert[key] = expert[key].toString()
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = expert[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        values.birthday = moment(values.birthday).format('YYYY-MM-DD')
        if (!err) {
          this.loading = true
          this.$put('/cos/student-info', {
            ...values
          }).then((r) => {
            this.$message.success('更新成功')
            this.loading = false
            this.getExpertInfo(this.currentUser.userId)
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
