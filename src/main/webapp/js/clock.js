/* ヘッダーの「ログイン中のユーザー」の横に現在日時を表示する（1秒ごとに更新） */
(function () {
  var WEEK = ['日', '月', '火', '水', '木', '金', '土'];

  function pad(n) {
    return (n < 10 ? '0' : '') + n;
  }

  function updateClock() {
    var el = document.getElementById('current-datetime');
    if (!el) return;
    var d = new Date();
    el.textContent = d.getFullYear() + '/' + pad(d.getMonth() + 1) + '/' + pad(d.getDate())
      + '（' + WEEK[d.getDay()] + '） '
      + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds());
  }

  updateClock();
  setInterval(updateClock, 1000);
})();
