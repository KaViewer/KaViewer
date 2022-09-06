function fn() {
    const pb = (tpl = {}, params = {}) => {
        tpl && params && Object.keys(params).forEach(k => {
            tpl[k] = params[k]

        })
        return tpl;
    }
    let url = 'http://localhost:9092'
    let env = karate.env;
    if (env) {
        url = 'kafka-service:9093'; // a custom 'intelligent' default
    }

    var config = {
        baseUrl: 'http://localhost:9394',
        bootstrapServer: url,
        payloadBuilder: pb,
    };
    return config;
}