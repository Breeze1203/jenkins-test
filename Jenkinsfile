
pipeline {
    agent any

    tools {
       nodejs 'NodeJS-16'
    }

    // --- 全局环境变量 ---
    environment {
        // 后端jar包的存放路径
        BACKEND_DEPLOY_PATH = '/home/user/app/backend'
        // 前端静态文件的存放路径 (Nginx的网站根目录)
        FRONTEND_DEPLOY_PATH = '/var/www/my-app'
    }

    stages {
        // 阶段 1: 拉取代码
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git...'
                checkout scm
            }
        }
        // 阶段 2: 构建后端
        stage('Build Backend') {
            steps {
                dir('backend') {
                    echo "Building Spring Boot application..."
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        // 阶段 3: 构建前端
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo "Building Vue.js application..."
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        // 阶段 4: 部署到本机
        stage('Deploy Locally') {
            steps {
                script {
                    echo "Deploying backend artifact locally..."
                    def jarFile = findFiles(glob: 'backend/target/*.jar')[0]
                    // 使用 cp 命令将 jar 文件复制到本地部署目录
                    sh "cp ${jarFile.path} ${BACKEND_DEPLOY_PATH}/app.jar"
                    // --- 部署前端 ---
                    echo "Deploying frontend artifacts locally..."
                    // 删除旧的前端文件
                    sh "rm -rf ${FRONTEND_DEPLOY_PATH}/*"
                    // 使用 cp 命令将构建好的 dist 目录内容复制到 Nginx 网站目录
                    sh "cp -r frontend/dist/* ${FRONTEND_DEPLOY_PATH}/"
                    // --- 重启后端服务 ---
                    echo "Restarting backend service..."
                    // 停止旧的后端服务 (|| true 确保即使没有找到进程也不会报错)
                    sh "pkill -f 'java -jar ${BACKEND_DEPLOY_PATH}/app.jar' || true"
                    // 启动新的后端服务
                    sh "nohup java -jar ${BACKEND_DEPLOY_PATH}/app.jar > /dev/null 2>&1 &"
                    echo "--- Local Deployment successful! ---"
                }
            }
        }
    }
}
