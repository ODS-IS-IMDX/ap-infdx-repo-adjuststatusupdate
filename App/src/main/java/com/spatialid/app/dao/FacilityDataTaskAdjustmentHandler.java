// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.spatialid.app.Common;
import com.spatialid.app.model.FacilityDataTaskAdjustmentManagementBean;

/**
 * 設備データ整備管理ハンドラークラス
 * 設備データ整備管理テーブルに対する操作を行う
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class FacilityDataTaskAdjustmentHandler {
    /**
     * B2設備データ整備ステータス変更SQL実行
     * 
     * @param connection 接続情報
     * @param taskInfo   整備タスク情報
     * @throws SQLException
     */
    public static int executeUpdateFacilityDataTaskAdjustmentStatement(
            Connection connection,
            FacilityDataTaskAdjustmentManagementBean taskInfo)
            throws SQLException {
        PreparedStatement ps = null;
        int updateCount = 0;

        try {
            ps = prepareUpdateFacilityDataTaskAdjustmentStatement(
                    connection, taskInfo);
            updateCount = ps.executeUpdate();
        } catch (SQLException e) {
            Common.outputLogMessage("error.ChangeStatus.00004",
                    Common.getCurrentMethodName(),
                    (ps == null) ? "" : ps.toString(),
                    Common.getErrorInfoString(e));
            throw e;
        }

        if (updateCount == 0) {
            // ログの出力(対象レコードなし)
            Common.outputLogMessage("error.ChangeStatus.00008",
                    Common.getCurrentMethodName(),
                    ps.toString());
        } else {
            // ログの出力(正常終了)
            Common.outputLogMessage("info.ChangeStatus.00006",
                    Common.getCurrentMethodName(),
                    ps.toString());
        }

        return updateCount;
    }

    /**
     * B2設備データ整備ステータス変更SQL作成
     * 
     * @param connection 接続情報
     * @param taskInfo   整備タスク情報
     * @return B2設備データ整備ステータス作成SQL
     * @throws SQLException
     */
    public static PreparedStatement prepareUpdateFacilityDataTaskAdjustmentStatement(
            Connection connection,
            FacilityDataTaskAdjustmentManagementBean taskInfo)
            throws SQLException {
        String sql = "update facility_data_adjustment_management"
                + " set process_end_date = ?, adjust_status = ?, adjust_message = ?, update_time = ?"
                + " where zip_file_name = ? and infra_company_id = ? and process_start_date = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setTimestamp(1, taskInfo.getProcessEndDate());
        pstmt.setString(2, taskInfo.getAdjustStatus());
        pstmt.setString(3, taskInfo.getAdjustMessage());
        pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

        pstmt.setString(5, taskInfo.getZipFileName());
        pstmt.setString(6, taskInfo.getInfraCompanyId());
        pstmt.setTimestamp(7, taskInfo.getProcessStartDate());

        return pstmt;
    }
}
