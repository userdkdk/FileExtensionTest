package com.example.server.business.fileExtension.api;

import com.example.server.business.fileExtension.api.request.CreateExtensionRequest;
import com.example.server.business.fileExtension.api.request.UpdateExtensionRequest;
import com.example.server.business.fileExtension.api.response.ExtensionResponse;
import com.example.server.business.fileExtension.application.FileExtensionService;
import com.example.server.global.common.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extension")
@RequiredArgsConstructor
public class FileExtensionController {

    private final FileExtensionService fileExtensionService;

    // create file extension
    @PostMapping("/")
    public ResponseEntity<CommonResponse<Void>> createExtension (
            @Valid @RequestBody CreateExtensionRequest request
    ) {
        fileExtensionService.createOrUpdateExtension(request);
        return CommonResponse.create();
    }

    // post file extension
    @PatchMapping("/")
    public ResponseEntity<CommonResponse<Void>> patchExtension (
            @Valid @RequestBody UpdateExtensionRequest request
    ) {
        fileExtensionService.updateExtension(request);
        return CommonResponse.ok();
    }

    // get file extension list
    @GetMapping("/")
    public ResponseEntity<CommonResponse<List<ExtensionResponse>>> getExtensionList () {
        List<ExtensionResponse> response = fileExtensionService.getExtensionList();
        return CommonResponse.ok(response);
    }

}
