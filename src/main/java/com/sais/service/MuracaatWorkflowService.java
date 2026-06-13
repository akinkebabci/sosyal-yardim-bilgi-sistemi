package com.sais.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class MuracaatWorkflowService {

    private final Map<Long, WorkflowState> workflowStates = new HashMap<>();

    public void markAileFertCompleted(Long muracaatId) {
        getOrCreateState(muracaatId).setAileFertGirildi(true);
    }

    public void markMaddiDurumCompleted(Long muracaatId) {
        getOrCreateState(muracaatId).setMaddiDurumGirildi(true);
    }

    public void markTutanakCompleted(Long muracaatId) {
        getOrCreateState(muracaatId).setTutanakGirildi(true);
    }

    public boolean isAileFertCompleted(Long muracaatId) {
        return getOrCreateState(muracaatId).isAileFertGirildi();
    }

    public boolean isMaddiDurumCompleted(Long muracaatId) {
        return getOrCreateState(muracaatId).isMaddiDurumGirildi();
    }

    public boolean isTutanakCompleted(Long muracaatId) {
        return getOrCreateState(muracaatId).isTutanakGirildi();
    }

    public void clearState(Long muracaatId) {
        workflowStates.remove(muracaatId);
    }

    private WorkflowState getOrCreateState(Long muracaatId) {
        return workflowStates.computeIfAbsent(muracaatId, id -> new WorkflowState());
    }

    private static class WorkflowState {
        private boolean aileFertGirildi = false;
        private boolean maddiDurumGirildi = false;
        private boolean tutanakGirildi = false;

        public boolean isAileFertGirildi() {
            return aileFertGirildi;
        }

        public void setAileFertGirildi(boolean aileFertGirildi) {
            this.aileFertGirildi = aileFertGirildi;
        }

        public boolean isMaddiDurumGirildi() {
            return maddiDurumGirildi;
        }

        public void setMaddiDurumGirildi(boolean maddiDurumGirildi) {
            this.maddiDurumGirildi = maddiDurumGirildi;
        }

        public boolean isTutanakGirildi() {
            return tutanakGirildi;
        }

        public void setTutanakGirildi(boolean tutanakGirildi) {
            this.tutanakGirildi = tutanakGirildi;
        }
    }
}

