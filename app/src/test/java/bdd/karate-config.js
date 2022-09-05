function fn() {
    const pb = (tpl = {}, params = {}) => {
        tpl && params && Object.keys(params).forEach(k => {
            tpl[k] = params[k]

        })
        return tpl;
    }
    return {
        baseUrl: 'http://localhost:9394',
        payloadBuilder: pb,
    };
}