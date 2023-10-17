
function goFetch(url, options = {}){
    const defaultHeaders = {
        'JY-ACCESS-TOKEN': getCookieValue('JY-ACCESS-TOKEN'),
    }

    const headers = new Headers({ ...defaultHeaders, ...options.headers });

    const newOptions = { ...options, headers };

    return fetch(url, newOptions);
}