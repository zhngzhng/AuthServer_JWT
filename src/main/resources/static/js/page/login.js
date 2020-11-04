new Vue({
    el: '#app',
    data: function () {
        var validateAccount = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter email address'));
            }else {
                callback();
            }
        };
        var validatePassword = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter password'));
            } else {
                callback();
            }
        };
        return {
            activeIndex: '6',
            //register form
            ruleForm: {
                account: '',
                password: '',
            },
            rules: {
                account: [
                    {validator: validateAccount, trigger: 'blur'}
                ],
                password: [
                    {validator: validatePassword, trigger: 'blur'}
                ],

            },

            inputVisible: false,
            inputValue: '',

            reset:false,
        }
    },
    methods: {
        //register form
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    // if (!this.ruleForm.remember) {
                    //     localStorage.removeItem('account');
                    //     localStorage.removeItem('password');
                    //     localStorage.setItem('remember', "no");
                    // }
                    this.login();
                } else {
                    this.$message({
                        showClose: true,
                        message: 'error submit!!',
                        type: 'error'
                    });
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        login() {
            var loginForm = new FormData();
            for (let key in this.ruleForm){
                loginForm.append(key, this.ruleForm[key]);
            }
            var options = {content: loginForm};
            $.ajax({
                url: '/AuthServer/in',
                type: 'post',
                // data对象中的属性名要和服务端控制器的参数名一致 login(name, password)
                // data: this.ruleForm,
                // data: this.ruleForm,
                // dataType : 'json',
                data: loginForm,
                success: () => {
                    // if (this.ruleForm.remember) {
                    //     localStorage.setItem('account', this.ruleForm.account);
                    //     localStorage.setItem('password', this.ruleForm.password);
                    //     localStorage.setItem('remember', "yes");
                    // }
                    this.$notify.error({
                        title: 'Error',
                        message: 'Login failed, email or password is wrong!',
                        offset:70
                    })
                    // if (result == "1") {
                    //     this.$notify.success({
                    //         title: 'Success',
                    //         message: 'Login successful ! Redirecting...',
                    //         offset: 70
                    //     });
                    //     // setTimeout(()=>{
                    //     //
                    //     //     if(preUrl!=null && preUrl.indexOf("/register") == -1){
                    //     //         window.location.href=preUrl;
                    //     //     }
                    //     //     else {
                    //     //         window.location.href = "/user/userSpace";
                    //     //     }
                    //     // },1000)
                    // }
                    // else {
                    //     this.$notify.error({
                    //         title: 'Error',
                    //         message: 'Login failed, email or password is wrong!',
                    //         offset: 70
                    //     });
                    //
                    // }
                },
                error: function (e) {
                    this.$message({
                        message: 'Submit Error!',
                        type: 'error',
                        offset: 40,
                        showClose: true,
                    });
                }
            });
        },
        resetPassword() {
            this.reset=true;
            this.$prompt('Please enter your email:', 'Reset Password', {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
                inputErrorMessage: 'E-mail format is incorrect.'
            }).then(({ value }) => {
                window.sessionStorage.setItem("email",value)
                let info=this.$notify.info({
                    title: 'Reseting password',
                    message: 'Please wait for a while, new password will be sent to your email.',
                    offset: 70,
                    duration: 0
                });
                $.ajax({
                    url: '/AuthServer/user/resetPassword',
                    type: 'post',
                    // data对象中的属性名要和服务端控制器的参数名一致 login(name, password)
                    data: {
                        email:value
                    },
                    // dataType : 'json',
                    success: (result) => {
                        info.close();
                        // this.reset=false;
                        if (result.data=="suc") {

                            this.$notify.success({
                                title: 'Success',
                                message: 'New password has been sent to your email. If you can not find the password, please check the spam box.',
                                offset: 70,
                                duration: 0
                            });

                            //邮件发送成功则跳转到修改密码页面
                            setTimeout(()=>{
                                window.location.href = "/AuthServer/resetPwd"
                            },1000)

                        }
                        else if(result.data=="no user") {
                            this.$notify({
                                title: 'Fail',
                                message: 'Email does not exist, please check again or register a new account.',
                                offset: 70,
                                type: 'warning',
                                duration: 0
                            });
                        }
                        else{
                            this.$notify.error({
                                title: 'Fail',
                                message: 'Reset failed,Please try again or contact opengms@njnu.edu.cn',
                                offset: 70,
                                duration: 0
                            });
                        }
                    },
                    error: function (e) {
                        alert("reset password error");
                    }
                });
            }).catch(() => {

            });
        }
    },
    mounted() {
        // this.ruleForm.remember = false;
        const remember = localStorage.getItem('remember');
        if ((remember != undefined) && (remember === "yes")) {
            this.ruleForm.account = localStorage.getItem("account");
            this.ruleForm.password = localStorage.getItem("password");
            this.ruleForm.remember = true;
        }

        $(document).keydown((event)=> {
            if (!this.reset && event.keyCode == 13) {
                this.login();
            }
        });

    }
})