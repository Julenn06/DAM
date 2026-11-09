package utils;

import dao.Backups;
import model.DAOException;

public class BackupsProzesua {
    public static void main(String[] args) {
        Backups backups = new Backups();
        try {
            backups.backupErabiltzaileak();
            backups.backupHistorikoa();
            backups.backupWorkouts();
        } catch (DAOException e) {
            System.exit(1);
        }
    }
}
