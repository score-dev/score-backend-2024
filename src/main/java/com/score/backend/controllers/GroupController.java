package com.score.backend.controllers;

import com.score.backend.models.dtos.GroupCreateDto;
import com.score.backend.models.dtos.GroupDto;
import com.score.backend.services.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController{

    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@Valid @RequestBody GroupCreateDto groupCreateDto, @RequestParam Long adminId) {
        try {
            groupCreateDto.isValid();  // 추가 검증
            groupService.createGroup(groupCreateDto, adminId);
            return ResponseEntity.ok("그룹 생성이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<String> leaveGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        try {
            groupService.removeMember(groupId, userId);
            return ResponseEntity.ok("그룹 탈퇴가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{groupId}/update")
    public ResponseEntity<String> updateGroup(@PathVariable Long groupId, @Valid @RequestBody GroupCreateDto groupCreateDto) {
        try {
            groupService.updateGroup(groupId, groupCreateDto);
            return ResponseEntity.ok("그룹 정보 update 완료!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{groupId}/removeMember")
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        try {
            groupService.removeMember(groupId, userId);
            return ResponseEntity.ok("멤버 강퇴가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
