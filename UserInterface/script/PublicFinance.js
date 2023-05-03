module.exports = class PublicFinace {
	constructor(){
		//Q: why do you use vietnamese as name for variables?
		//A: I have no clue what those terminologies are for, nobody explain to for me, i basically have the document and that is that.
		document.querySelector("#public-finance-form-1").addEventListener("click", () => this.#chungTu16a1Hoac16a2("Thông tin chung của mẫu chứng từ 16a1 (C2-02a/NS)"));
		document.querySelector("#public-finance-form-2").addEventListener("click", () => this.#chungTu16a1Hoac16a2("Thông tin chung của mẫu chứng từ 16a2 (C2-02b/NS)"));
		document.querySelector("#public-finance-form-3").addEventListener("click", () => this.#hachToan16a1Hoac16a2("Thông tin hạch toán của mẫu chứng từ 16a1 (C2-02a/NS)"));
		document.querySelector("#public-finance-form-4").addEventListener("click", () => this.#hachToan16a1Hoac16a2("Thông tin hạch toán của mẫu chứng từ 16a2 (C2-02b/NS)"));
		document.querySelector("#public-finance-form-8").addEventListener("click", () => this.#bangKe07());
		document.querySelector("#public-finance-form-9").addEventListener("click", () => this.#chiTietBang07());
		document.querySelector("#public-finance-form-10").addEventListener("click", () => this.#chungTu05a());
		document.querySelector("#public-finance-form-11").addEventListener("click", () => this.#hachToan05a());
		document.querySelector("#public-finance-form-12").addEventListener("click", () => this.#chungTu06());
		document.querySelector("#public-finance-form-13").addEventListener("click", () => this.#hachToan06());
	}

	#chungTu16a1Hoac16a2(title){
		let items = ["Mã hồ sơ gửi lên hệ thống DVC",
		"Số chứng từ sinh theo quy định DVC",
		"Là Tiền Mặt",
		"Số Thứ Tự",
		"Tổng số tiền của chứng từ",
		"Số chứng từ giấy trên phần mềm ứng dụng",
		"Số tài khoản thực hiện giao dịch với KBNN",
		"Mã đơn vị tiền tệ chứng từ sử dụng",
		"Ngày chứng từ trên phần mềm ứng dụng",
		"Là thực chi hay tạm ứng",
		"Là chuyển khoản hay tiền mặt",
		"Ứng trước điều kiện thanh toán",
		"Hợp đồng khung",
		"Hợp đồng thực hiện",
		"Mã đơn vị quan hệ ngân sách",
		"Tên viết tắt đơn vị quan hệ ngân sách",
		"Số tài khoản đơn vị thực hiện giao dịch",
		"Mã KBNN thực hiện giao dịch",
		"Mã cấp ngân sách của đơn vị",
		"Năm ngân sách",
		"Mã chương trình mục tiêu",
		"Tên đơn vị nộp thuế",
		"Mã số thuế đơn vị nộp thuế",
		"Mã nội dung kinh tế đơn vị nộp thuế",
		"Mã chương đơn vị nộp thuế",
		"Mã cơ quan thu đơn vị nộp thuế,",
		"Số tài khoản đơn vị thực hiện giao dịch",
		"Mã KBNN thực hiện giao dịch",
		"Mã cấp ngân sách của đơn vị",
		"Năm ngân sách",
		"Mã chương trình mục tiêu",
		"Tên đơn vị nộp thuế",
		"Mã số thuế đơn vị nộp thuế",
		"Mã nội dung kinh tế đơn vị nộp thuế",
		"Mã chương đơn vị nộp thuế",
		"Mã cơ quan thu đơn vị nộp thuế",
		"Thông tin kỳ thuế",
		"Mã quan hệ ngân sách đơn vị giao dịch",
		"Mã đơn vị giao dịch",
		"Dữ liệu ký số của chủ tài khoản theo cấu trúc DVC",
		"Dữ liệu ký số của kế toán trưởng theo cấu trúc DVC",
		"Loại cơ quan thanh toán",
		"Thời gian kế toán trưởng ký",
		"Thời gian chủ tài khoản ký",
		"Kế toán trưởng",
		"Chủ tài khoản"];
		this.#createDocument(title, items);
	}

	#hachToan16a1Hoac16a2(title){
		let items = ["Số chứng từ trên hệ thống  DVC",
		"Nội dung chi tiết",
		"Mã nội dung kinh tế",
		"Mã chương",
		"Mã ngành",
		"Số tiền",
		"Mã nguồn chi",
		"Số tiền nộp thuế",
		"Số tiền nhận",
		"Số thứ tự dòng chi tiết"];
		this.#createDocument(title, items);
	}

	#chungTu16c1Hoac16c3(){
		const items = ["Mã Hồ sơ gửi DVC",
			"ID tài liệu",
			"Số chứng từ sinh theo quy định DVC",
			"Có sử dụng tiền mặt hay không",
			"Số thứ tự của các chứng từ",
			"Tổng số tiền của chứng từ",
			"Số chứng từ lấy trên phần mềm ứng dụng",
			"Số tài khoản giao dịch với kho bạc",
			"Đơn vị tiền tệ sử dụng",
			"Ngày lập chứng từ",
			"Mã đơn vị quan hệ ngân sách giao dịch",
			"Tên viết tắt của đơn vị",
			"Địa chỉ đơn vị",
			"Mã đơn vị nộp thuế",
			"Tên đơn vị nộp thuế",
			"Mã nội dung kinh tế đơn vị nộp thuế",
			"Mã chương đơn vị nộp thuế",
			"Mã cơ quan thu đơn vị nộp thuế",
			"Thông tin kho bạc hạch toán",
			"Tên đơn vị nhận tiền",
			"Địa chỉ đơn vị nhận tiền",
			"Số tài khoản đơn vị nhận tiền",
			"Năm ngân sách",
			"Thông tin kỳ thuế",
			"Thông tin số tờ khai nộp thuế",
			"Tên kho bạc đơn vị trả tiền",
			"Tên kho bạc đơn vị nhận tiền",
			"Loại cơ quan thanh toán đơn vị trả tiền",
			"Loại cơ quan thanh toán đơn vị nhận tiền",
			"Số tài khoản đơn vị trả tiền",
			"Mã cấp ngân sách đơn vị trả tiền",
			"Mã ngân hàng đơn vị trả tiền",
			"Mã ngân hàng đơn vị nhận tiền",
			"Số tiền nhận",
			"Tổng số tiền của chứng từ",
			"Thực chi hay là tạm ứng",
			"Chuyển khoản hay là tiền mặt",
			"Họ và tên người nhận",
			"Số CMND người nhận",
			"Nơi cấp CMND người nhận",
			"Ngày cấp CMND người nhận",
			"Mã nhà tài trợ",
			"Mã đơn vị QHNS của đơn vị giao dịch",
			"Mã đơn vị giao dịch",
			"Dữ liệu ký số của chủ tài khoản theo cấu trúc DVC",
			"Dữ liệu ký số của kế toán trường theo cấu trúc DVC",
			"Thời gian kế toán trưởng ký",
			"Thời gian chủ tài khoản ký",
			"Kế toán trưởng",
			"Chủ tài khoản"];
		this.#createDocument("Thông tin chung của mẫu chứng từ 16c1 (C4-02a/KB)", items);
	}

	#bangKe07(){
		const items = [
			"Mã hồ sơ gửi DVC",
			"ID tài liệu bảng kê 07(99)",
			"Số chứng từ sinh theo quy định DVC:",
			"Số thứ tự của các chứng từ",
			"Tổng số tiền của chứng từ",
			"Số chứng từ lấy trên phần mềm ứng dụng",
			"Ngày lập chứng từ",
			"Mã đơn vị quan hệ ngân sách giao dịch",
			"Mã chương trình mục tiêu",
			"Mã nguồn",
			"Mã cấp ngân sách",
			"Mã QHNS của đơn vị giao dịch",
			"Mã đơn vị giao dịch",
			"Dữ liệu ký số của chủ tài khoản theo cấu trúc DVC",
			"Dữ liệu ký số của kế toán trường theo cấu trúc DVC",
			"Thời gian kế toán trưởng ký",
			"Thời gian chủ tài khoản ký",
			"Kế toán trưởng",
			"Chủ tài khoản"
		];
		this.#createDocument("Thông tin chung của bảng kê 07 (M01)", items);
	}

	#chiTietBang07(){
		const items = [
			"Số hóa đơn",
			"Số chứng từ",
			"Ngày lập chứng từ",
			"Mã nội dung kinh tế",
			"Nội dung chi tiết",
			"Số tiền hạch toán",
			"Số thứ tự dòng chi tiết",
			"Ngày hóa đơn",
			"Số tiền",
			"Số tiền"];
		this.#createDocument("Thông tin chi tiết của bảng kê 07 (M01)", items);
	}

	#chungTu05a(){
		const items = [
				"Mã hồ sơ gửi lên hệ thống DVC",
				"Thông tin tài liệu ID theo DVC",
				"Số chứng từ sinh theo quy định DVC",
				"Tổng số tiền của chứng từ",
				"Số chứng từ giấy trên phần mềm ứng dụng",
				"Số tài khoản thực hiện giao dịch với KBNN",
				"Mã đơn vị tiền tệ chứng từ sử dụng",
				"Ngày chứng từ trên phần mềm ứng dụng",
				"Là thực chi hay tạm ứng",
				"Mã quan hệ ngân sách",
				"Số tờ khai",
				"Mã KBNN",
				"Mã cấp ngân sách",
				"Năm ngân sách",
				"Mã chương trình mục tiêu",
				"Căn cứ số dư: là tạm ứng hay ứng trước",
				"Ngày căn cứ số dư tạm ứng/ứng trước",
				"Mã KBNN đề nghị thanh toán căn cứ Tạm ứng/ứng trước",
				"Tên KBNN đề nghị thanh toán căn cứ Tạm ứng/ứng trước",
				"Thanh toán số tiền đã Tạm ứng hay ứng trước",
				"Thanh toán số tiền đã Tạm ứng hay ứng trước thành Thực chi hay ứng trước đủ điều kiện thanh toán",
				"Tổng số tiền trên chứng từ",
				"Mã QHNS của đơn vị giao dịch",
				"Mã đơn vị giao dịch",
				"Dữ liệu ký số của chủ tài khoản theo cấu trúc DVC",
				"Dữ liệu ký số của kế toán trường theo cấu trúc DVC",
				"Kế toán trưởng",
				"Chủ tài khoản",
				"Thời gian kế toán trưởng ký",
				"Thời gian chủ tài khoản ký"];
		this.#createDocument("Thông tin chung của mẫu chứng từ 05a (C2-03/NS)", items);
	}

	#hachToan05a(){
		const items = [
			"Số chứng từ trên hệ thống  DVC",
			"Nội dung chi tiết",
			"Mã nội dung kinh tế",
			"Mã chương",
			"Mã ngành kinh tế",
			"Mã nguồn chi",
			"Số ứng trước",
			"Số đề nghị",
			"Số phê duyệt",
			"Số thứ tự"
		];
		this.#createDocument("Thông tin hạch toán của mẫu chứng từ 05a (C2-03/NS)", items);
	}

	#chungTu06(){
		const items = [
			"Mã hồ sơ gửi lên hệ thống DVC",
			"Thông tin tài liệu ID theo DVC",
			"Số chứng từ sinh theo quy định DVC",
			"Tổng số tiền của chứng từ",
			"Số chứng từ giấy trên phần mềm ứng dụng",
			"Số tài khoản thực hiện giao dịch với KBNN",
			"Mã đơn vị tiền tệ chứng từ sử dụng",
			"Ngày chứng từ trên phần mềm ứng dụng",
			"Là chuyển khoản hay tiền mặt",
			"Tên đơn vị",
			"Số tờ khai",
			"Người nộp",
			"Mã KBNN đơn vị nộp",
			"Tài khoản đơn vị nộp",
			"Quyết định số",
			"Mã ngân hàng đơn vị nộp",
			"Tên KBNN đơn vị nộp",
			"Loại đơn vị nộp",
			"Loại tờ khai",
			"Quyết định ngày",
			"Mã đơn vị dự toán",
			"Số tờ khai đơn vị dự toán",
			"Mã KBNN đơn vị dự toán",
			"Cấp ngân sách đơn vị dự toán",
			"Năm ngân sách",
			"Cam kết chi hợp đồng khung",
			"Cam kết chi hợp đồng thực hiện",
			"Mã chương trình mục tiêu",
			"Tổng số tiền trên chứng từ",
			"Mã QHNS đơn vị giao dịch",
			"Mã đơn vị giao dịch",
			"Chữ ký số chủ tài khoản",
			"Chữ ký số kế toán trưởng",
			"Ngày KTT ký số",
			"Ngày CTK ký số"];
		this.#createDocument("Thông tin chung của mẫu chứng từ 06 (C2-05a/NS)", items);
	}

	#hachToan06(){
		const items = [
			"Số chứng từ",
			"Nội dung chi tiết",
			"Mã nội dung kinh tế",
			"Mã chương",
			"Mã ngành kinh tế",
			"Mã nguồn chi",
			"Số tiền",
			"Năm kế hoạch"];
		this.#createDocument("Thông tin hạch toán của mẫu chứng từ 06 (C2-05a/NS)", items);
	}

	#createDocument(title, items){
		const titleSpan = document.createElement("span");
		titleSpan.innerText = title;
		const deleteInstanceButton = document.createElement("a");
		deleteInstanceButton.classList.add("button");
		deleteInstanceButton.classList.add("red-button");
		deleteInstanceButton.innerText = "-";
		const collapseButton = document.createElement("a");
		collapseButton.classList.add("button");
		const buttonWrapper = document.createElement("div");
		buttonWrapper.appendChild(deleteInstanceButton);
		buttonWrapper.appendChild(collapseButton);
		const header = document.createElement("div");
		header.classList.add("header");
		header.appendChild(titleSpan);
		header.appendChild(buttonWrapper);
		const unorderdList = document.createElement("ul");
		for (let i of items) this.#createListItem(i, unorderdList);
		const container = document.createElement("li");
		container.appendChild(header);
		container.appendChild(unorderdList);
		deleteInstanceButton.addEventListener("click", () => document.querySelector("#public-finance-list").removeChild(container));
		collapseButton.addEventListener("click", () => unorderdList.classList.toggle("float-div-inactive"));
		document.querySelector("#public-finance-list").appendChild(container);

	}

	#createListItem(title, unorderdList){
		const spanBody = document.createElement("span");
		const body = document.createElement("div");
		body.appendChild(spanBody);
		const spanTitle = document.createElement("span");
		spanTitle.innerText = title;
		const spanContent = document.createElement("span");
		const editButton = document.createElement("a");
		editButton.classList.add("button");
		editButton.classList.add("edit-button");
		editButton.addEventListener("click", () => {
			const input = document.createElement("input");
			input.value = spanBody.innerText;
			input.addEventListener("blur", () => {
				body.removeChild(input);
				spanBody.innerText = input.value;
			});
			spanBody.innerText = "";
			body.appendChild(input);
		});
		const header = document.createElement("div");
		header.classList.add("header");
		header.appendChild(spanTitle);
		header.appendChild(spanContent);
		header.appendChild(editButton);
		const container = document.createElement("li");
		container.appendChild(header);
		container.appendChild(body);
		unorderdList.appendChild(container);
	}
}
