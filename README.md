
# Medipush Android application

<center><img src="https://github.com/Medipush/Medipush/blob/master/medipush_icon.png?raw=true" style="width:400px, height:auto"></center>

## SDK version

- minSdkVersion 16
- targetSdkVersion 30

---
# File explanation
- MainActivity : 로그인 및 서버에서 데이터를 가져옵니다.
- mainScreen : 어플리케이션의 메인 화면을 담당하고 각 UI간의 통신 어시스트 역할을 합니다.
- Cautions : 서버에 저장되어있는 주의사항을 출력해줍니다.
- Morning : 아침 알람을 설정할 수 있고, 아침에 먹여야하는 약물들을 리스트로 표시하며, 각 약물들의 삭제를 진행합니다.
- 약물 리스트는 RecyclerView로 생성하며 PostAdapter와 PostViewHolder로 관리합니다.
- MorningReceiver : Notification이 발생했을 경우 실행되는 Receiver로 알림을 띄우고 다음 알림을 설정하도록 합니다.
- MorninfBoot : 재부팅됐을 경우 알람서비스가 종료되므로 다시 켜주는 역할을 합니다.
- Lunch와 Dinner 또한 마차가지로 실행합니다.

# Application explanation
이 어플은 환자의 복용시간알람 및 복용 알람 관리, 주의사항 안내 등의 역할을 합니다.
아침, 점심, 저녁 별로 알람을 매일 울리게 키거나 끌 수 있도록 하여 약 복용 시간을 환자가 잊지 않도록 도와줍니다.
또한 현재 복용중인 약물을 아침, 점심, 저녁에 구분지어 리스트로 나타내어주며 해당 약물을 전부 복용했다면, 서버와 안드로이드 모두에서 삭제를 진행해 약사가 처방 시 오진이 없도록 도와줍니다.
마지막으로 환자별로 서버에 저장되어있는 복용시 주의사항을 리스트로 만들어서 환자가 약물 복용 시 부작용을 줄일 수 있도록 합니다.
