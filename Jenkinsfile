// 用于构建和部署 Spring Boot + Vue 应用的 Jenkinsfile (本地部署)

pipeline {
    // 1. 定义在哪里执行
    agent any

    // 2. 定义需要什么工具 (与 agent 平级)
    tools {
        // 这行代码现在会去查找您在 Jenkins Tools 中配置的 NodeJS-16
        nodejs 'NodeJS-16'
    }

    // 3. 定义环境变量
    environment {
        // 后端 jar 包的部署路径
        BACKEND_DEPLOY_PATH = '/home/user/app/backend'
        // 前端静态文件的部署路径 (Nginx 的网站根目录)
        FRONTEND_DEPLOY_PATH = '/var/www/my-app'
    }

    // 4. 定义阶段
    stages {
        // 阶段 1: 拉取代码
        stage('Checkout') {
            steps {
                echo '从 Git 仓库拉取源代码...'
                checkout scm
            }
        }

        // 阶段 2: 构建后端
        stage('Build Backend') {
            steps {
                dir('backend') {
                    echo "正在构建 Spring Boot 应用..."
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        // 阶段 3: 构建前端 (现在会使用 Node.js v16)
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo "正在构建 Vue.js 应用..."
                    // 打印版本以确认
                    sh 'node -v'
                    sh 'npm -v'
                    // 安装依赖并构建
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        // 阶段 4: 本地部署
        stage('Deploy Locally') {
            steps {
                script {
                    // --- 准备部署目录 ---
                    echo "正在准备部署目录..."
                    // 使用 mkdir -p 安全地创建目录，如果目录已存在则什么也不做
                    sh "mkdir -p ${BACKEND_DEPLOY_PATH}"
                    sh "mkdir -p ${FRONTEND_DEPLOY_PATH}"

                    // --- 部署后端 ---
                    echo "正在本地部署后端构建产物..."
                    def jarFile = findFiles(glob: 'backend/target/*.jar')[0]
                    sh "cp ${jarFile.path} ${BACKEND_DEPLOY_PATH}/app.jar"

                    // --- 部署前端 ---
                    echo "正在本地部署前端构建产物..."
                    sh "rm -rf ${FRONTEND_DEPLOY_PATH}/*"
                    sh "cp -r frontend/dist/* ${FRONTEND_DEPLOY_PATH}/"

                    // --- 重启后端服务 ---
                    echo "正在重启后端服务..."
                    sh "pkill -f 'java -jar ${BACKEND_DEPLOY_PATH}/app.jar' || true"
                    sh "nohup java -jar ${BACKEND_DEPLOY_PATH}/app.jar > /dev/null 2>&1 &"

                    echo "--- 本地部署成功! ---"
                }
            }
        }
    }
}
