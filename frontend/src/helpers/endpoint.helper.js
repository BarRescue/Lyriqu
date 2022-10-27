export const endpoints = {
    music_endpoint,
    payment_endpoint,
    subscription_endpoint,
    monitor_endpoint
}

function music_endpoint() {
    return process.env.NODE_ENV === 'development' ? 'http://localhost:7630/music/' : 'https://api.lyriqu.nl/music';
}

function payment_endpoint() {
    return process.env.NODE_ENV === 'development' ? 'http://localhost:7628/payment/' : 'https://api.lyriqu.nl/payment';
}

function subscription_endpoint() {
    return process.env.NODE_ENV === 'development' ? 'http://localhost:7629/subscription/' : 'https://api.lyriqu.nl/subscription';
}

function monitor_endpoint() {
    return process.env.NODE_ENV === 'development' ? 'http://localhost:7632/monitoring/' : 'https://api.lyriqu.nl/monitoring';
}
