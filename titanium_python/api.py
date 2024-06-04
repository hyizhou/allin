import py_eureka_client.eureka_client as eureka_client
from flask import Flask, render_template, request, make_response, jsonify, abort
from newspaper import Article, ArticleException

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False


def register_eureka():
    """
    注册 eureka
    """
    server_list = "http://localhost:8088/eureka"  # 多个注册中心用逗号分割
    my_server_host = "127.0.0.1"  # 服务host
    my_server_port = 8081  # 服务端口
    eureka_client.init(eureka_server=server_list,
                       app_name="titaniumPython",
                       instance_host=my_server_host,
                       instance_port=my_server_port
                       )


def newspaper(url: str):
    """
    解析 url 内容，返回正文内容
    """
    article = Article(url, language='zh')
    article.download()
    article.parse()
    # print('text:', article.text)
    article.nlp()
    # print('summary:', article.summary)
    return article.text, article.summary


@app.post('/url_summary')
def url_summary():
    """
    对提供的url进行总结并返回
    返回格式：
    [{
        'url': '',
        'text': '',  # 正文内容
        'summary': ''  # 总结内容
    },...]
    """

    data2 = []
    url = request.get_json(silent=False, cache=False, force=False).get('url')
    for i in url:
        print(i)
        try:
            result = newspaper(i)
        except ArticleException as e:
            print("链接解析错误")
            continue
        data = {'text': None, 'summary': result[1], 'url': i}
        data2.append(data)
    return jsonify(data2)


@app.post('/link_summary')
def link_summary():
    """
    对提供的url进行总结并返回
    """
    link = request.get_json(silent=False, cache=False, force=False).get('link')
    try:
        result = newspaper(link)
    except ArticleException as e:
        print("链接解析错误")
        print(e)
        abort(400)
    data = {'text': None, 'summary': result[1], 'url': link}
    return jsonify(data)


if __name__ == '__main__':
    app.run()
