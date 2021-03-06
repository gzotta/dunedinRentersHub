/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dao.ServicesJdbcDAO;
import domain.Landlord;
import domain.Renter;
import domain.Services;
import org.jooby.Jooby;
import org.jooby.Result;
import org.jooby.Status;

/**
 *
 * @author zotta
 */
public class ServicesModule extends Jooby {

    public ServicesModule(ServicesJdbcDAO servicesDao) {

        //Save (POST) a service.
        post("/api/registerService", (req, rsp) -> {
            Services service = req.body().to(Services.class);
            servicesDao.saveService(service);
            rsp.status(Status.CREATED);
        });

        //GET services by type.
        get("/api/services/:serviceType", (req) -> {
            String serviceType = req.param("serviceType").value();
            return servicesDao.filterByType(serviceType);

        });
        
        
        //GET a services by username.
        get("/api/services/:username", (req) -> {
            String username = req.param("username").value();
            if (servicesDao.getServices(username) == null) {
                return new Result().status(Status.NOT_FOUND);
            } else {
                return servicesDao.getServices(username);
            }
        });

    }

}
