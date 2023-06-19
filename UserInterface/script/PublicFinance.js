
module.exports = class {
	constructor(){}

	#mau16c1Hoac16c3(excelData){
		const data = {
			GNHS_HOSO:{
				MA_HO_SO:, //Mã hồ sơ
				DM_KBNN_ID:, //Mã KBNN thực hiện giao dịch
				DM_HS_KSC_ID:, //Mã loại hồ sơ
				DM_LOAIDOITUONG_ID:, //Mã loại đối tượng
				GN_LOAIHS_ID:, //Mã loại hồ sơ
				MTK_DVGD_ID:, //Mã đơn vị giao dịch
				HINH_THUC_GIAO_NHAN: 0, //Hình thức giao nhận
				LOAI: 1, //Loại chứng từ
				MTK_DVGD_DVQHNS_ID:, //Mã quan hệ ngân sách
				DVGD_MA:, //Mã đơn vị giao dịch
				DVGD_SOTHANHLAP:, //Số quyết định thành lập
				DVGD_NGAYTHANHLAP:, //Ngày quyết định thành lập
				SU_DUNG_CKS: 1, //Sử dụng CKS
				GN_HOSO_TAILIEU:{ //Thẻ đánh dấu bắt đầu tài liệu trong hồ sơ
					GN_TAILIEU_ID:, //Mã của loại tài liệu
					SO_CHUNG_TU:, //Số chứng từ giấy
					NGAY_CHUNG_TU:, //Ngày chứng từ
					DM_DV_TRATIEN_ID:, //Mã đơn vị quan hệ ngân sách
					MTK_DV_TRATIEN_ID:, //Mã đơn vị quan hệ ngân sách!?
					DV_TRATIEN_DIACHI:, //Địa chỉ đơn vị
					DV_TRATIEN_KBNN_ID:, //Mã KBNN mở tài khoản
					DV_TRATIEN_TKKT_ID:, //Mã Tài khoản
					DV_TRATIEN_CTMT_ID:, //Mã chương trình mục tiêu
					DM_DV_NOPTHUE_ID:, //Mã đơn vị nộp thuế
					MTK_DV_NOPTHUE_ID:,
					DV_NOPTHUE_NDKT_ID:, //Mã nội dung kinh tế phần thuế
					DV_NOPTHUE_CHUONG_ID:, //Mã chương nộp thuế
					DV_NOPTHUE_CQTHU_ID:, //Mã cơ quan thu
					DV_NOPTHUE_HACHTOAN:, //Mã KBNN hạch toán thu
					DV_NOPTHUE_SOTIEN:, //Số tiền nộp thuế
					DM_DV_NHANTIEN_ID:, //Mã đơn vị nhận tiền
					MTK_DV_NHANTIEN_ID:,
					DV_NHANTIEN_DIACHI:, //Địa chỉ đơn vị
					DV_NHANTIEN_SOTK_SO:, //Tài khoản đơn vị nhận tiền
					DV_NHANTIEN_CTMT_ID:,
					DV_NHANTIEN_SOTIEN:, //Số tiền thanh toán
					TONG_SO_TIEN:, //Tổng số tiền
					NGAY_TAO:, //Ngày tạo
					DV_TRATIEN_KBNN_NH_TEN:, //Nơi mở tài khoản của đơn vị trả tiền
					DV_NHANTIEN_KBNN_NH_TEN:, //Nơi mở tài khoản đơn vị nhận tiền
					DV_TRATIEN_LOAI:, //Loại tài khoản đơn vị trả tiền
					DV_NHANTIEN_LOAI:, //Loại tài khoản đơn vị nhận tiền
					DV_NHANTIEN_KBNN_ID:, //Mã KBNN nhận tiền
					MTK_NGUOITAO_ID:,
					GN_HOSO_C402_GT:{ //Thẻ chi tiết chứng từ C402
						NOI_DUNG:, //Nội dung
						SO_TIEN:, //Số tiền
						NOP_THUE:, //Số tiền nộp thuế
						THANH_TOAN:, //Số tiền thanh toán
						MA_HANG: //Số thứ tự dòng
					}
				}
			}
		};
	}
	#mau16a1Hoac16a3(excelData){
		let data = {}
		GNHS_HOSO:{
			MA_HO_SO:, //Mã hồ sơ
			DM_KBNN_ID:, //Mã KBNN thực hiện giao dịch
			DM_HS_KSC_ID:, //Mã loại hồ sơ
			DM_LOAIDOITUONG_ID:, //Mã loại đối tượng
			GN_LOAIHS_ID:, //Mã loại hồ sơ
			MTK_DVGD_ID:, //Mã đơn vị giao dịch
			HINH_THUC_GIAO_NHAN: 0, //Hình thức giao nhận
			LOAI: 1, //Loại chứng từ
			MTK_DVGD_DVQHNS_ID:, //Mã quan hệ ngân sách
			DVGD_MA:, //Mã đơn vị giao dịch
			DVGD_SOTHANHLAP:, //Số quyết định thành lập
			DVGD_NGAYTHANHLAP:, //Ngày quyết định thành lập
			SU_DUNG_CKS: 1, //Sử dụng CKS
			GN_HOSO_TAILIEU:{
				GN_TAILIEU_ID:, //Mã của loại tài liệu
				NGAY_CHUNG_TU:, //Ngày chứng từ
				SO_CHUNG_TU:, //Số chứng từ giấy
				THUCCHI_TAMUNG:, //Thực chi/Tạm ứng
				CHUYENKHOAN_TIENMAT:, //Chuyển khoản/ Tiền mặt
				UT_DKTT:, //Ứng trước đủ điều kiện thanh toán
				DM_DVQHNS_ID:, //Mã đơn vị quan hệ ngân sách
				MTK_DVGD_DVQHNS_ID:, //Mã đơn vị quan hệ ngân sách
				DVQHNS_MA:, //Mã đơn vị quan hệ ngân sách
				DVQHNS_TEN:, //Tên đơn vị quan hệ ngân sách
				DVQHNS_SOTK_ID:, //Mã tài khoản mở tại KBNN
				DVQHNS_SOTK_SO:, //Mã tài khoản mở tại KBNN
				DVQHNS_KBNN_ID:, //Mã KBNN mở tài khoản
				DVQHNS_CAPNS_ID:, //Mã cấp ngân sách
				DVQHNS_NAMNS:, //Năm ngân sách
				DVQHNS_CTMT_ID:, //Mã chương trình mục tiêu
				DVQHNS_CKC_HDK:, //Số hợp đồng khung
				DVQHNS_CKC_HDTH:, //Số hợp đồng thực hiện
				DM_DV_NOPTHUE_ID:, //Mã đơn vị nộp thuế
				DV_NOPTHUE_TEN:, //Tên đơn vị nộp thuế
				DV_NOPTHUE_MASOTHUE:, //Mã số thuế
				DV_NOPTHUE_NDKT_ID:, //Mã nội dung kinh tế phần thuế
				DV_NOPTHUE_CHUONG_ID:, //Mã chương nộp thuế
				DV_NOPTHUE_CQTHU_ID:, //Mã cơ quan thu
				DV_NOPTHUE_CQTHU_MA:, //Mã cơ quan thu
				DV_NOPTHUE_KB_HACHTOAN_ID:, //Mã KBNN hạch toán thu
				DV_NOPTHUE_SOTIENNOP:, //Số tiền nộp thuế
				DM_DV_NHANTIEN_ID
			}
<></DM_DV_NHANTIEN_ID>
<MTK_DV_NHANTIEN_ID></ MTK_DV_NHANTIEN_ID>
<DV_NHANTIEN_MA>< /DV_NHANTIEN_MA>
<DV_NHANTIEN_DIACHI></DV_NHANTIEN_DIACHI>
<DV_NHANTIEN_SOTK_SO></DV_NHANTIEN_SOTK_SO>
<DV_NHANTIEN_KBNN_ID></ DV_NHANTIEN_KBNN_ID>
<DV_NHANTIEN_NGANHANG_ID></ DV_NHANTIEN_NGANHANG_ID>
<DV_NHANTIEN_CTMT_ID></ DV_NHANTIEN_CTMT_ID>
<DV_NHANTIEN_SOTIENNHAN></DV_NHANTIEN_SOTIENNHAN>
<MTK_NGUOINHAN_ID></ MTK_NGUOINHAN_ID>
<NGUOINHAN_SO_CMND></ NGUOINHAN_SO_CMND>
<NGUOINHAN_HOTEN></ NGUOINHAN_HOTEN>
<NGUOINHAN_NGAYCAP_CMND></ NGUOINHAN_NGAYCAP_CMND>
<NGUOINHAN_NOICAP_CMND></NGUOINHAN_NOICAP_CMND>
<TONG_SO_TIEN></TONG_SO_TIEN>
<NGAY_TAO></NGAY_TAO>
<GN_HOSO_C202_GT>
<NOI_DUNG></NOI_DUNG>
<DM_NDKT_ID></DM_NDKT_ID>
<DM_CHUONG_ID></DM_CHUONG_ID>
<DM_NGANH_KT_ID></DM_NGANH_KT_ID>
<DM_NGUONCHI_ID></DM_NGUONCHI_ID>
<SO_TIEN></SO_TIEN>
<DV_NOPTHUE></DV_NOPTHUE>
<DV_NHANTIEN></DV_NHANTIEN>
<MA_HANG></MA_HANG>
</GN_HOSO_C202_GT>
</GN_HOSO_TAILIEU>
    </GNHS_HOSO>
	}
}
