from py_eureka_client import eureka_client

from api import app, register_eureka

# 按装订区域中的绿色按钮以运行脚本。
if __name__ == '__main__':
    register_eureka()
    try:
        app.run(port=8081)
    finally:
        eureka_client.stop()
