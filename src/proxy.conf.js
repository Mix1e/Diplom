const HttpsProxyAgent = require('https-proxy-agent');

const proxyConfig = [{
  context: '/v1',
  target: 'https://apidata.mos.ru',
  secure: false,
  changeOrigin: true,
}];

function setupForCorporateProxy(proxyConfig) {
  var proxyServer = process.env.http_proxy || process.env.HTTP_PROXY;
  if (proxyServer) {
    var agent = new HttpsProxyAgent(proxyServer);
    console.log('Using corporate proxy server: ' + proxyServer);
    proxyConfig.forEach(function(entry) {
      entry.agent = agent;
    });
  }
  return proxyConfig;
}

module.exports = setupForCorporateProxy(proxyConfig);

