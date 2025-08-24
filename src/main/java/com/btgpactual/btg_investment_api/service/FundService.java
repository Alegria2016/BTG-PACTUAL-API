package com.btgpactual.btg_investment_api.service;

import com.btgpactual.btg_investment_api.model.Fund;
import com.btgpactual.btg_investment_api.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FundService {
    @Autowired
    private FundRepository fundRepository;

    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    public Optional<Fund> getFundById(String id) {
        return fundRepository.findById(id);
    }

    public Fund createFund(Fund fund) {
        return fundRepository.save(fund);
    }

    public void initializeFunds() {
        if (fundRepository.count() == 0) {
            fundRepository.save(new Fund("FPV_BTG_PACTUAL_RECAUDADORA", 75000.0, "FPV"));
            fundRepository.save(new Fund("FPV_BTG_PACTUAL_ECOPETROL", 125000.0, "FPV"));
            fundRepository.save(new Fund("DEUDAPRIVADA", 50000.0, "FIC"));
            fundRepository.save(new Fund("FDO-ACCIONES", 250000.0, "FIC"));
            fundRepository.save(new Fund("FPV_BTG_PACTUAL_DINAMICA", 100000.0, "FPV"));
        }
    }
}
