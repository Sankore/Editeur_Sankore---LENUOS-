$(document).ready(function(){
	(function(){
		function Item(baseUrl) {
			var finish = false;
			var finishCallback = null;
			
			this.baseUrl = baseUrl;
			this.url = "";
			this.saveOnUnload = false;
			this.score = null;
			this.time = null;
			this.data = null;
			
			this.isFinish = function() {return finish;};
			this.finish = function(score, time, data) {finish = true; this.score = score; this.time = time; this.data = data; if(finishCallback != null) finishCallback.call();};
			this.setFinishCallback = function(callback) {finishCallback = callback;};
			this.getUrl = function () {return this.baseUrl + this.url;};
			this.getStartUrl = function() {return this.getUrl()+"start";};
			this.getOpenUrl = function() {return this.getUrl()+"open";};
			this.getSaveUrl = function() {return this.getUrl()+"save";};
			this.getFinishUrl = function() {return this.getUrl()+"close?finish=true";};
			this.getCloseUrl = function() {return this.getUrl()+"close";};
		}
		
		Item.prototype.init = function(url, saveOnUnload, success, error, finishCallback) {
			this.url = url;
			this.setFinishCallback(finishCallback);
			this.saveOnUnload = saveOnUnload;
			$.get(this.getStartUrl(), function(data, textStatus) {
				if(textStatus == "success" && data != undefined && data != ""){
					success(data);
				}else
					error(data, textStatus);
			});
		};
		
		Item.prototype.getSave = function() {
			return {"score":10, "time":50000, "data":'[["p1t1","p1t2"],["p2t1","p2t2"], ["p3t1","p3t2"]]'};
		};
		
		Item.prototype.open = function() {
			$.get(this.getOpenUrl());
		};
		Item.prototype.save = function(score, time, data) {
			$.post(this.getSaveUrl(),{"score":score,"time":time,"data":data});
		};
		Item.prototype.close = function() {
			$.post(this.isFinish() ? this.getFinishUrl() : this.getCloseUrl(),{"score":this.score,"time":this.time,"data":this.data});
		};
		window.Item = new Item("");
	})();
});
