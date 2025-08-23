pipeline {
    agent any

    stages {
        // 第一个阶段：Build
        stage('Build') {
            steps {
                echo 'Gathering tool versions...'
                // 使用 sh (shell) 步骤来执行命令
                // 1. 输出一个标题到 version_info.txt 文件 (>) 会创建或覆盖文件
                // 2. 将 java -version 的输出追加到文件 (>>)
                //    注意: java -version 通常输出到标准错误(stderr), 所以用 2>&1 将其重定向到标准输出(stdout)
                sh 'echo "--- Tool Versions Report ---" > version_info.txt'
                sh 'java -version >> version_info.txt 2>&1'
            }
        }

        // 第二个阶段：Test
        stage('Test') {
            steps {
                echo 'Gathering more tool versions...'
                // 将 mvn 和 git 的版本信息追加到同一个文件中
                sh 'mvn -version >> version_info.txt'
                sh 'git --version >> version_info.txt'
            }
        }

        // 第三个阶段：Deploy (这里我们只用来展示最终结果)
        stage('Deploy') {
            steps {
                echo 'Finalizing and displaying the report.'
                // 在文件末尾追加一条完成信息
                sh 'echo "\n--- Report Generation Complete ---" >> version_info.txt'

                // 为了方便在 Jenkins 日志中直接查看，我们把文件的最终内容打印出来
                echo '--- Final Version Report ---'
                sh 'cat version_info.txt'
            }
        }
    }
}

