document.addEventListener("DOMContentLoaded", async () => {
	await new Promise((resolve, reject) => {
		let count = 0;
		const timer = setInterval(() => {
			if (window.javaConnector) {
				clearInterval(timer);
				resolve();
			} else if (++count > 20) {
				clearInterval(timer);
				reject();
			}
		}, 100);
	}).catch(() => AlertMessage.danger("System error!!"));

	//ページ遷移
	document.addEventListener("click", function page(e) {
		const target = e.target.closest("[data-target-page]");
		if (!target) return true;
		const page = target.dataset.targetPage;
		JavaCaller.loadPage(page)
	});

	//javaメッセージ出力
	document.addEventListener("click", function msg(e) {
		const target = e.target.closest("[data-msg]");
		if (!target) return true;
		const msg = target.dataset.msg;
		JavaCaller.logMessage(msg)
	});
});




window.alertMessage = (message, type = 'danger', duration = 3000) => {
	alert(message);
	const div = document.createElement('div');
	div.className = `alert alert-${type} alert-dismissible fade show custom-alert`;
	div.role = 'alert';
	div.innerHTML = `${message}<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;

	document.body.appendChild(div);

	setTimeout(() => {
		div.classList.remove('show');
		div.classList.add('hide');
		setTimeout(() => div.remove(), 300);
	}, duration);
}

class AlertMessage {
	static success = (message) => window.alertMessage(message, 'success');
	static danger = (message) => window.alertMessage(message, 'danger');
	static warning = (message) => window.alertMessage(message, 'warning');
	static info = (message) => window.alertMessage(message, 'info');
}

class JavaCaller {
	static loadPage = (page) => window.javaConnector.call(new Array("loadPage", page));
	static logMessage = (message) => window.javaConnector.call(new Array("logMsg", message));
}
