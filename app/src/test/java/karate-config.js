function fn() {
    const pb = (tpl = {}, params = {}) => {
        tpl && params && Object.keys(params).forEach(k => {
            tpl[k] = params[k]

        })
        return tpl;
    }
    var config = {
        baseUrl: 'http://localhost:9394',
        payloadBuilder: pb,
    };

    return config;
}