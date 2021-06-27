package com.platformbuilders.clients.controller;

import com.platformbuilders.clients.model.Client;
import com.platformbuilders.clients.repository.ClientRepository;
import com.platformbuilders.clients.service.ClientService;
import com.platformbuilders.clients.specification.ClientFilterRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/clients")
@Api(value = "Client API Rest")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ClientsController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    @ApiOperation(value = "Get all paged clients", httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "query successfully completed"), @ApiResponse(code = 400, message = "failed to fetch data")})
    public ResponseEntity<?> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int pageIndex,
                                     @RequestParam(value = "size", required = false, defaultValue = "20") int pageSize,
                                     ClientFilterRequest clientFilterRequest) {
        PageRequest pageable = PageRequest.of(pageIndex, pageSize);
        return new ResponseEntity<>(clientRepository.findAll(clientFilterRequest.buildSpecification(), pageable), HttpStatus.OK);
    }

    @GetMapping("{oid}")
    @ApiOperation(value = "Get client by oid", httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "returned successfully"), @ApiResponse(code = 400, message = "failed to fetch data")})
    public ResponseEntity<?> findByOid(@PathVariable("oid") String oid) {
        return new ResponseEntity<>(clientService.findClientByOid(oid), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Save client", httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "saved successfully"), @ApiResponse(code = 400, message = "failed to save client")})
    public ResponseEntity<Client> save(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.save(client));
    }

    @DeleteMapping(path = "{oid}")
    @ApiOperation(value = "Delete client", httpMethod = "DELETE")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "delete successfully"), @ApiResponse(code = 400, message = "failed to delete client")})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("oid") String oid) {
        clientService.delete(oid);
    }
}
