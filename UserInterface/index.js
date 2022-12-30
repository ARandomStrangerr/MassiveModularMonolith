const electronModule = require("electron");
const electron = electronModule.app;

let mainWindow;
let remote;
function createWindow(){
	mainWindow = new electronModule.BrowserWindow(
		{
			webPreferences:
			{
				nodeIntegration: true,
            	contextIsolation: false,
			},
			width: 800,
			height: 400
		}
	);
	mainWindow.loadURL(`file://${__dirname}/Mainpage.html`);
	mainWindow.webContents.openDevTools();
	mainWindow.on("closed",
		function(){
			mainWindow = null;
			remote = null;
		}
	);
}

electron.on("ready", createWindow);
electron.on("window-all-closed",
	function() {
		electron.quit();
	}
);