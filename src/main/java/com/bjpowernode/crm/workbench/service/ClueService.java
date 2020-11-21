package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueRemark;

public interface ClueService {
    void saveClue(Clue clue);

    Clue queryClueDetailById(String id);

    void updateClueRemark(ClueRemark clueRemark);

    void saveClueRemark(ClueRemark clueRemark);
}
