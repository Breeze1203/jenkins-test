pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        nodejs 'NodeJS-16'
    }

    environment {
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
                    sh """
                        # 杀掉旧进程
                        pkill -f '${BACKEND_DEPLOY_PATH}/app.jar' || true
                        sleep 2
                        cd ${BACKEND_DEPLOY_PATH}

                        # 启动新进程
                        BUILD_ID=dontKillMe nohup java -jar app.jar > backend.log 2>&1 & disown

                        # 等待应用启动
                        echo "等待应用启动..."
                        for i in {1..10}; do
                            if netstat -tulnp | grep -q ${BACKEND_PORT}; then
                                echo "✅ 后端应用已成功启动，端口 ${BACKEND_PORT} 正在监听"
                                exit 0
                            fi
                            sleep 3
                        done

                        echo "❌ 应用启动失败，请检查日志：${BACKEND_DEPLOY_PATH}/backend.log"
                        tail -n 50 ${BACKEND_DEPLOY_PATH}/backend.log
                        exit 1
                    """

                    echo "--- 本地部署脚本执行完毕! ---"
                }
            }
        }
    }
}
