//package com.score.backend.config;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.score.backend.domain.group.GroupEntity;
//import com.score.backend.domain.school.School;
//import com.score.backend.member.User;
//import com.score.backend.domain.group.repositories.GroupRepository;
//import com.score.backend.domain.school.repositories.SchoolRepository;
//import com.score.backend.member.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Service;
//
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class InitDataConfig implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final SchoolRepository schoolRepository;
//    private final GroupRepository groupRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        for (InitDataType type : InitDataType.values()) {
//            List<?> list = getInitSamplesFromFile(type);
//            if (!list.isEmpty()) {
//                switch (type) {
//                    case USER: userRepository.saveAll((List<User>) list); break;
//                    case GROUP: groupRepository.saveAll((List<GroupEntity>) list); break;
//                    case SCHOOL: schoolRepository.saveAll((List<School>) list); break;
//                    default: throw new IllegalArgumentException("init data type " + type.name() + " not found.");
//                }
//            }
//        }
//    }
//
//    private List<?> getInitSamplesFromFile(InitDataType type) throws IOException {
//        List<?> list;
//        String FILE_INIT_SAMPLE = "init-data.json";
//
//        switch (type) {
//            case USER:  list = new ArrayList<User>(); FILE_INIT_SAMPLE = "UserSample.json"; break;
//            case GROUP: list = new ArrayList<GroupEntity>(); FILE_INIT_SAMPLE = "GroupSample.json"; break;
//            case SCHOOL: list = new ArrayList<School>(); FILE_INIT_SAMPLE = "SchoolSample.json"; break;
//            default: throw new IllegalArgumentException("init data type " + type.name() + " not found.");
//        }
//        try (InputStream inputStream = getStreamFromResource(FILE_INIT_SAMPLE)) {
//            JsonNode sampleNode = getSampleNode(inputStream);
//            list = getSampleListFromNode(sampleNode, type);
//        }
//        return list;
//    }
//
//    private InputStream getStreamFromResource(String fileLocation) {
//        ClassLoader classLoader = InitDataConfig.class.getClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream(fileLocation);
//
//        if (inputStream == null) {
//            throw new IllegalArgumentException("init data file \"" + fileLocation + "\" not found.");
//        } else {
//            return inputStream;
//        }
//    }
//
//    private JsonNode getSampleNode(InputStream inputStream) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode sampleNode = null;
//        try (DataInputStream dis = new DataInputStream(inputStream)) {
//            sampleNode = objectMapper.readTree(dis).path("sample");
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
//        return sampleNode;
//    }
//
//    private List<?> getSampleListFromNode(JsonNode sampleNode, InitDataType type) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        switch (type) {
//            case USER: return new ArrayList<>(objectMapper.convertValue(sampleNode, new TypeReference<List<User>>() {}));
//            case GROUP: return new ArrayList<>(objectMapper.convertValue(sampleNode, new TypeReference<List<GroupEntity>>() {}));
//            case SCHOOL: return new ArrayList<>(objectMapper.convertValue(sampleNode, new TypeReference<List<School>>() {}));
//            default: throw new IllegalArgumentException("init data type " + type.name() + " not found.");
//        }
//    }
//}