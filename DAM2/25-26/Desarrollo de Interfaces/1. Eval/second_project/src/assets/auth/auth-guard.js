(function() {
  if (localStorage.getItem('loggedInUser')) {
    window.location.href = '/welcome';
  }
})();