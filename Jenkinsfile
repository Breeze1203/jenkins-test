// 用于构建和部署 Spring Boot + Vue 应用的 Jenkinsfile (本地部署)

pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        nodejs 'NodeJS-16'
    }

    environment {
        // 明确设置 JAVA_HOME 和 PATH，这是一个非常好的实践
        JAVA_HOME = tool(name: 'JDK-17', type: 'jdk')
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        BACKEND_DEPLOY_PATH = '/home/user/app/backend'
        FRONTEND_DEPLOY_PATH = '/var/www/my-app'
        BACKEND_PORT = '9999'  // Spring Boot 监听端口
    }

    stages {
        stage('Checkout') {
            steps {
                echo '从 Git 仓库拉取源代码...'
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    echo "正在构建 Spring Boot 应用..."
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo "正在构建 Vue.js 应用..."
                    sh 'node -v'
                    sh 'npm -v'
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Deploy Locally') {
            steps {
                script {
                    echo "准备部署目录..."
                    sh "mkdir -p ${BACKEND_DEPLOY_PATH}"
                    sh "mkdir -p ${FRONTEND_DEPLOY_PATH}"

                    echo "部署后端构建产物..."
                    def jarFile = findFiles(glob: 'backend/target/*.jar')[0]
                    sh "cp ${jarFile.path} ${BACKEND_DEPLOY_PATH}/app.jar"

                    echo "部署前端构建产物..."
                    sh "rm -rf ${FRONTEND_DEPLOY_PATH}/*"
                    sh "cp -r frontend/dist/* ${FRONTEND_DEPLOY_PATH}/"

                    echo "重启后端服务..."
                    sh "pkill -f '${BACKEND_DEPLOY_PATH}/app.jar' || true"
                    sh "(nohup java -jar ${BACKEND_DEPLOY_PATH}/app.jar > ${BACKEND_DEPLOY_PATH}/backend.log 2>&1 &)"

                    // --- **关键修正**: 使用 Groovy 循环进行健康检查 ---
                    echo "等待后端服务启动 (最多等待30秒)..."
                    for (int i = 0; i < 10; i++) {
                        // 使用 sh(returnStatus: true) 来捕获命令的退出码，而不是让流水线直接失败
                        def status = sh(script: "netstat -tuln | grep -q ${BACKEND_PORT}", returnStatus: true)
                        if (status == 0) {
                            echo "✅ 后端服务已在端口 ${BACKEND_PORT} 上成功启动!"
                            // 成功找到端口，跳出循环
                            break
                        }
                        if (i < 9) {
                            echo "检查失败，3秒后重试..."
                            sleep 3
                        } else {
                            // 10次尝试后仍然失败，则使用 Jenkins 的 error 步骤来主动报错
                            sh "tail -n 50 ${BACKEND_DEPLOY_PATH}/backend.log" // 在失败前打印日志
                            error "❌ 后端服务在30秒内未能启动，请检查日志!"
                        }
                    }

                    echo "--- 本地部署脚本执行完毕! ---"
                }
            }
        }
    }
}
