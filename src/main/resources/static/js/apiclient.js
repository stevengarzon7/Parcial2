apiclient = (function () {
    let url = "https://afternoon-wave-37957.herokuapp.com/v1/";
     return {
        getCasesByCity: function (city, callback) {
            $.getJSON(url + "?place=" + city, (data) => {
                callback(data);
            }, null)
        },
    }
})();