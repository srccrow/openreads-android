import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_email_sender/flutter_email_sender.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:openreads/core/constants.dart/enums.dart';
import 'package:openreads/core/themes/app_theme.dart';
import 'package:openreads/logic/bloc/rating_type_bloc/rating_type_bloc.dart';
import 'package:openreads/logic/bloc/theme_bloc/theme_bloc.dart';
import 'package:openreads/ui/backup_screen/backup_screen.dart';
import 'package:openreads/ui/settings_screen/widgets/widgets.dart';
import 'package:openreads/ui/trash_screen/trash_screen.dart';
import 'package:openreads/ui/unfinished_screen/unfinished_screen.dart';
import 'package:settings_ui/settings_ui.dart';
import 'package:url_launcher/url_launcher.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  static const version = '2.0.0-beta3';
  static const repoUrl = 'https://github.com/mateusz-bak/openreads-android';
  static const translationUrl = 'https://crwd.in/openreads-android';
  static const communityUrl = 'https://matrix.to/#/#openreads:matrix.org';
  static const rateUrl = 'market://details?id=software.mdev.bookstracker';
  final releaseUrl = '$repoUrl/releases/tag/$version';
  final licenceUrl = '$repoUrl/blob/master/LICENSE';
  final githubIssuesUrl = '$repoUrl/issues';

  _sendEmailToDev(BuildContext context, [bool mounted = true]) async {
    final Email email = Email(
      subject: 'Openreads feedback',
      body: 'Version 2.0.0-beta3\n',
      recipients: ['mateusz.bak.dev@gmail.com'],
      isHTML: false,
    );

    try {
      await FlutterEmailSender.send(email);
    } catch (error) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            '$error',
            style: TextStyle(
              fontFamily: context.read<ThemeBloc>().fontFamily,
            ),
          ),
        ),
      );
    }
  }

  _openGithubIssue(BuildContext context, [bool mounted = true]) async {
    try {
      await launchUrl(
        Uri.parse(githubIssuesUrl),
        mode: LaunchMode.externalApplication,
      );
    } catch (error) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            '$error',
            style: TextStyle(
              fontFamily: context.read<ThemeBloc>().fontFamily,
            ),
          ),
        ),
      );
    }
  }

  _setThemeModeAuto(BuildContext context, SetThemeState state) {
    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: ThemeMode.system,
      showOutlines: state.showOutlines,
      cornerRadius: state.cornerRadius,
      primaryColor: state.primaryColor,
      fontFamily: state.fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _setThemeModeLight(BuildContext context, SetThemeState state) {
    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: ThemeMode.light,
      showOutlines: state.showOutlines,
      cornerRadius: state.cornerRadius,
      primaryColor: state.primaryColor,
      fontFamily: state.fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _setThemeModeDark(BuildContext context, SetThemeState state) {
    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: ThemeMode.dark,
      showOutlines: state.showOutlines,
      cornerRadius: state.cornerRadius,
      primaryColor: state.primaryColor,
      fontFamily: state.fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _setFont(
    BuildContext context,
    SetThemeState state,
    Font font,
  ) {
    String? fontFamily;

    switch (font) {
      case Font.montserrat:
        fontFamily = 'Montserrat';
        break;
      case Font.lato:
        fontFamily = 'Lato';
        break;
      case Font.sofiaSans:
        fontFamily = 'SofiaSans';
        break;
      case Font.poppins:
        fontFamily = 'Poppins';
        break;
      case Font.raleway:
        fontFamily = 'Raleway';
        break;
      case Font.nunito:
        fontFamily = 'Nunito';
        break;
      case Font.playfairDisplay:
        fontFamily = 'PlayfairDisplay';
        break;
      case Font.kanit:
        fontFamily = 'Kanit';
        break;
      case Font.lora:
        fontFamily = 'Lora';
        break;
      case Font.quicksand:
        fontFamily = 'Quicksand';
        break;
      case Font.barlow:
        fontFamily = 'Barlow';
        break;

      default:
        fontFamily = null;
        break;
    }

    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: state.themeMode,
      showOutlines: state.showOutlines,
      cornerRadius: state.cornerRadius,
      primaryColor: state.primaryColor,
      fontFamily: fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _showOutlines(BuildContext context, SetThemeState state) {
    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: state.themeMode,
      showOutlines: true,
      cornerRadius: state.cornerRadius,
      primaryColor: state.primaryColor,
      fontFamily: state.fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _hideOutlines(BuildContext context, SetThemeState state) {
    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: state.themeMode,
      showOutlines: false,
      cornerRadius: state.cornerRadius,
      primaryColor: state.primaryColor,
      fontFamily: state.fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _changeCornerRadius(
      BuildContext context, SetThemeState state, double radius) {
    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: state.themeMode,
      showOutlines: state.showOutlines,
      cornerRadius: radius,
      primaryColor: state.primaryColor,
      fontFamily: state.fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _setAccentColor(BuildContext context, SetThemeState state, Color color) {
    BlocProvider.of<ThemeBloc>(context).add(ChangeThemeEvent(
      themeMode: state.themeMode,
      showOutlines: state.showOutlines,
      cornerRadius: state.cornerRadius,
      primaryColor: color,
      fontFamily: state.fontFamily,
    ));

    Navigator.of(context).pop();
  }

  _showAccentColorDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: Theme.of(context).extension<CustomBorder>()?.radius ??
                BorderRadius.circular(5.0),
          ),
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 20),
            child: BlocBuilder<ThemeBloc, ThemeState>(
              builder: (context, state) {
                if (state is SetThemeState) {
                  final theme = Theme.of(context);
                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 10),
                        child: Text(
                          'Select accent color',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            fontFamily: context.read<ThemeBloc>().fontFamily,
                          ),
                        ),
                      ),
                      const SizedBox(height: 15),
                      _buildAccentButton(
                          context, state, theme.primaryGreen, 'Green'),
                      _buildAccentButton(
                          context, state, theme.primaryBlue, 'Blue'),
                      _buildAccentButton(
                          context, state, theme.primaryRed, 'Red'),
                      _buildAccentButton(
                          context, state, theme.primaryYellow, 'Yellow'),
                      _buildAccentButton(
                          context, state, theme.primaryOrange, 'Orange'),
                      _buildAccentButton(
                          context, state, theme.primaryPurple, 'Purple'),
                      _buildAccentButton(
                          context, state, theme.primaryPink, 'Pink'),
                      _buildAccentButton(
                          context, state, theme.primaryTeal, 'Teal'),
                    ],
                  );
                } else {
                  return const SizedBox();
                }
              },
            ),
          ),
        );
      },
    );
  }

  Row _buildAccentButton(
    BuildContext context,
    SetThemeState state,
    Color color,
    String colorName,
  ) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 10),
          child: Container(
            width: 50,
            height: 25,
            decoration: BoxDecoration(
              borderRadius: Theme.of(context).extension<CustomBorder>()?.radius,
              color: color,
            ),
          ),
        ),
        Expanded(
          child: SettingsDialogButton(
            text: colorName,
            onPressed: () => _setAccentColor(
              context,
              state,
              color,
            ),
          ),
        ),
      ],
    );
  }

  _showRatingBarDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: Theme.of(context).extension<CustomBorder>()?.radius ??
                BorderRadius.circular(5.0),
          ),
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 20),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Padding(
                  padding: const EdgeInsets.only(left: 10),
                  child: Text(
                    'Select rating display type',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      fontFamily: context.read<ThemeBloc>().fontFamily,
                    ),
                  ),
                ),
                const SizedBox(height: 15),
                SettingsDialogButton(
                  text: 'Rating as a bar',
                  onPressed: () {
                    BlocProvider.of<RatingTypeBloc>(context).add(
                      const RatingTypeChange(ratingType: RatingType.bar),
                    );

                    Navigator.of(context).pop();
                  },
                ),
                const SizedBox(height: 5),
                SettingsDialogButton(
                  text: 'Rating as a number',
                  onPressed: () {
                    BlocProvider.of<RatingTypeBloc>(context).add(
                      const RatingTypeChange(ratingType: RatingType.number),
                    );

                    Navigator.of(context).pop();
                  },
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  _showThemeModeDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: Theme.of(context).extension<CustomBorder>()?.radius ??
                BorderRadius.circular(5.0),
          ),
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 20),
            child: BlocBuilder<ThemeBloc, ThemeState>(
              builder: (context, state) {
                if (state is SetThemeState) {
                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 10),
                        child: Text(
                          'Select theme mode',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            fontFamily: context.read<ThemeBloc>().fontFamily,
                          ),
                        ),
                      ),
                      const SizedBox(height: 15),
                      SettingsDialogButton(
                        text: 'Follow system mode',
                        onPressed: () => _setThemeModeAuto(context, state),
                      ),
                      const SizedBox(height: 5),
                      SettingsDialogButton(
                        text: 'Light mode',
                        onPressed: () => _setThemeModeLight(context, state),
                      ),
                      const SizedBox(height: 5),
                      SettingsDialogButton(
                        text: 'Dark mode',
                        onPressed: () => _setThemeModeDark(context, state),
                      ),
                    ],
                  );
                } else {
                  return const SizedBox();
                }
              },
            ),
          ),
        );
      },
    );
  }

  _showFontDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: Theme.of(context).extension<CustomBorder>()?.radius ??
                BorderRadius.circular(5.0),
          ),
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 20),
            child: BlocBuilder<ThemeBloc, ThemeState>(
              builder: (context, state) {
                if (state is SetThemeState) {
                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 10),
                        child: Text(
                          'Select font',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            fontFamily: context.read<ThemeBloc>().fontFamily,
                          ),
                        ),
                      ),
                      const SizedBox(height: 15),
                      Expanded(
                        child: Scrollbar(
                          thumbVisibility: true,
                          child: SingleChildScrollView(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                SettingsDialogButton(
                                  text: 'System default',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.system,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Barlow',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.barlow,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Kanit',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.kanit,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Lato',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.lato,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Lora',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.lora,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Montserrat',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.montserrat,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Nunito',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.nunito,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'PlayfairDisplay',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.playfairDisplay,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Poppins',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.poppins,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Raleway',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.raleway,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Sofia Sans',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.sofiaSans,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                SettingsDialogButton(
                                  text: 'Quicksand',
                                  onPressed: () => _setFont(
                                    context,
                                    state,
                                    Font.quicksand,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ],
                  );
                } else {
                  return const SizedBox();
                }
              },
            ),
          ),
        );
      },
    );
  }

  _showOutlinesDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: Theme.of(context).extension<CustomBorder>()?.radius ??
                BorderRadius.circular(5.0),
          ),
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 20),
            child: BlocBuilder<ThemeBloc, ThemeState>(
              builder: (context, state) {
                if (state is SetThemeState) {
                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 10),
                        child: Text(
                          'Select level of corner radius',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            fontFamily: context.read<ThemeBloc>().fontFamily,
                          ),
                        ),
                      ),
                      const SizedBox(height: 15),
                      SettingsDialogButton(
                        text: 'Show outlines',
                        onPressed: () => _showOutlines(context, state),
                      ),
                      const SizedBox(height: 5),
                      SettingsDialogButton(
                        text: 'Hide outlines',
                        onPressed: () => _hideOutlines(context, state),
                      ),
                    ],
                  );
                } else {
                  return const SizedBox();
                }
              },
            ),
          ),
        );
      },
    );
  }

  _showCornerRadiusDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: Theme.of(context).extension<CustomBorder>()?.radius ??
                BorderRadius.circular(5.0),
          ),
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 20),
            child: BlocBuilder<ThemeBloc, ThemeState>(
              builder: (context, state) {
                if (state is SetThemeState) {
                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 10),
                        child: Text(
                          'Display outlines in the UI',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            fontFamily: context.read<ThemeBloc>().fontFamily,
                          ),
                        ),
                      ),
                      const SizedBox(height: 15),
                      SettingsDialogButton(
                        text: 'No rounded corners',
                        onPressed: () => _changeCornerRadius(context, state, 0),
                      ),
                      const SizedBox(height: 5),
                      SettingsDialogButton(
                        text: 'Small radius',
                        onPressed: () => _changeCornerRadius(context, state, 5),
                      ),
                      const SizedBox(height: 5),
                      SettingsDialogButton(
                        text: 'Medium radius',
                        onPressed: () =>
                            _changeCornerRadius(context, state, 10),
                      ),
                      const SizedBox(height: 5),
                      SettingsDialogButton(
                        text: 'Big radius',
                        onPressed: () =>
                            _changeCornerRadius(context, state, 20),
                      ),
                    ],
                  );
                } else {
                  return const SizedBox();
                }
              },
            ),
          ),
        );
      },
    );
  }

  SettingsTile _buildURLSetting({
    required String title,
    String? description,
    required String url,
    IconData? iconData,
    required BuildContext context,
  }) {
    return SettingsTile.navigation(
      title: Text(
        title,
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: (iconData == null) ? null : Icon(iconData),
      description: (description != null)
          ? Text(
              description,
              style: TextStyle(
                fontFamily: context.read<ThemeBloc>().fontFamily,
              ),
            )
          : null,
      onPressed: (_) {
        launchUrl(
          Uri.parse(url),
          mode: LaunchMode.externalNonBrowserApplication,
        );
      },
    );
  }

  SettingsTile _buildFeedbackSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Send feedback',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      description: Text(
        'Report bugs or new ideas',
        style: TextStyle(
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.record_voice_over_rounded),
      onPressed: (context) {
        FocusManager.instance.primaryFocus?.unfocus();

        showModalBottomSheet(
          context: context,
          isScrollControlled: true,
          backgroundColor: Colors.transparent,
          builder: (context) {
            return Column(
              mainAxisAlignment: MainAxisAlignment.end,
              mainAxisSize: MainAxisSize.min,
              children: [
                Container(
                  height: 3,
                  width: MediaQuery.of(context).size.width / 4,
                  decoration: BoxDecoration(
                      color: Colors.grey.shade300,
                      borderRadius: BorderRadius.circular(10)),
                ),
                const SizedBox(height: 5),
                Container(
                  padding: const EdgeInsets.fromLTRB(10, 20, 10, 40),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: Theme.of(context).scaffoldBackgroundColor,
                  ),
                  child: IntrinsicHeight(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        Expanded(
                          child: ContactButton(
                            text: 'Send the developer an email',
                            icon: FontAwesomeIcons.solidEnvelope,
                            onPressed: () => _sendEmailToDev(context),
                          ),
                        ),
                        const SizedBox(width: 20),
                        Expanded(
                          child: ContactButton(
                            text: 'Raise an issue on Github',
                            icon: FontAwesomeIcons.github,
                            onPressed: () => _openGithubIssue(context),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            );
          },
        );
      },
    );
  }

  SettingsTile _buildTrashSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Deleted books',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(FontAwesomeIcons.trash),
      onPressed: (context) {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const TrashScreen(),
          ),
        );
      },
    );
  }

  SettingsTile _buildUnfinishedSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Unfinished books',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.not_interested),
      onPressed: (context) {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const UnfinishedScreen(),
          ),
        );
      },
    );
  }

  SettingsTile _buildBackupSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Backup and restore',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.settings_backup_restore_rounded),
      onPressed: (context) {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const BackupScreen(),
          ),
        );
      },
    );
  }

  SettingsTile _buildAccentSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Accent color',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.color_lens),
      description: BlocBuilder<ThemeBloc, ThemeState>(
        builder: (_, themeState) {
          if (themeState is SetThemeState) {
            switch (themeState.primaryColor.value) {
              case 0xffB73E3E:
                return Text(
                  'Red',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              case 0xff2146C7:
                return Text(
                  'Blue',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              case 0xff285430:
                return Text(
                  'Green',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              case 0xffE14D2A:
                return Text(
                  'Orange',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              case 0xff9F73AB:
                return Text(
                  'Purple',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              case 0xffFF577F:
                return Text(
                  'Pink',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              case 0xff3FA796:
                return Text(
                  'Teal',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              default:
                return Text(
                  'Yellow',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
            }
          } else {
            return const SizedBox();
          }
        },
      ),
      onPressed: (context) => _showAccentColorDialog(context),
    );
  }

  SettingsTile _buildCornersSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Rounded corners',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.rounded_corner_rounded),
      description: BlocBuilder<ThemeBloc, ThemeState>(
        builder: (_, themeState) {
          if (themeState is SetThemeState) {
            if (themeState.cornerRadius == 5) {
              return Text(
                'Small radius',
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            } else if (themeState.cornerRadius == 10) {
              return Text(
                'Medium radius',
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            } else if (themeState.cornerRadius == 20) {
              return Text(
                'Big radius',
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            } else {
              return Text(
                'No radius',
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            }
          } else {
            return const SizedBox();
          }
        },
      ),
      onPressed: (context) => _showCornerRadiusDialog(context),
    );
  }

  SettingsTile _buildOutlinesSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Display outlines in the UI',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.check_box_outline_blank_rounded),
      description: BlocBuilder<ThemeBloc, ThemeState>(
        builder: (_, themeState) {
          if (themeState is SetThemeState) {
            if (themeState.showOutlines) {
              return Text(
                'Show outlines',
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            } else {
              return Text(
                'Hide outlines',
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            }
          } else {
            return const SizedBox();
          }
        },
      ),
      onPressed: (context) => _showOutlinesDialog(context),
    );
  }

  SettingsTile _buildThemeModeSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'App theme mode',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.sunny),
      description: BlocBuilder<ThemeBloc, ThemeState>(
        builder: (_, themeState) {
          if (themeState is SetThemeState) {
            switch (themeState.themeMode) {
              case ThemeMode.light:
                return Text(
                  'Light mode',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              case ThemeMode.dark:
                return Text(
                  'Dark mode',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
              default:
                return Text(
                  'Follow system',
                  style: TextStyle(
                    fontFamily: context.read<ThemeBloc>().fontFamily,
                  ),
                );
            }
          } else {
            return const SizedBox();
          }
        },
      ),
      onPressed: (context) => _showThemeModeDialog(context),
    );
  }

  SettingsTile _buildFontSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Font',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.font_download),
      description: BlocBuilder<ThemeBloc, ThemeState>(
        builder: (_, state) {
          if (state is SetThemeState) {
            if (state.fontFamily != null) {
              return Text(
                state.fontFamily!,
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            } else {
              return Text(
                'System default',
                style: TextStyle(
                  fontFamily: context.read<ThemeBloc>().fontFamily,
                ),
              );
            }
          } else {
            return const SizedBox();
          }
        },
      ),
      onPressed: (context) => _showFontDialog(context),
    );
  }

  SettingsTile _buildRatingTypeSetting(BuildContext context) {
    return SettingsTile(
      title: Text(
        'Rating bar display type',
        style: TextStyle(
          fontSize: 16,
          fontFamily: context.read<ThemeBloc>().fontFamily,
        ),
      ),
      leading: const Icon(Icons.star_rounded),
      description: BlocBuilder<RatingTypeBloc, RatingTypeState>(
        builder: (_, state) {
          if (state is RatingTypeNumber) {
            return Text(
              'As a number',
              style: TextStyle(
                fontFamily: context.read<ThemeBloc>().fontFamily,
              ),
            );
          } else if (state is RatingTypeBar) {
            return Text(
              'As a bar',
              style: TextStyle(
                fontFamily: context.read<ThemeBloc>().fontFamily,
              ),
            );
          } else {
            return const SizedBox();
          }
        },
      ),
      onPressed: (context) => _showRatingBarDialog(context),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      appBar: AppBar(
        backgroundColor: Theme.of(context).scaffoldBackgroundColor,
        surfaceTintColor: Theme.of(context).scaffoldBackgroundColor,
        title: Text(
          'Settings',
          style: TextStyle(
            fontSize: 18,
            fontFamily: context.read<ThemeBloc>().fontFamily,
          ),
        ),
      ),
      body: SettingsList(
        contentPadding: const EdgeInsets.only(top: 10),
        sections: [
          SettingsSection(
            tiles: <SettingsTile>[
              _buildURLSetting(
                title: 'Join the community',
                description: 'To be up to date with the Openreads project',
                url: communityUrl,
                iconData: FontAwesomeIcons.peopleGroup,
                context: context,
              ),
              // TODO: Show only on GPlay variant
              _buildURLSetting(
                title: 'Rate the application',
                description: 'You like Openreads? Click here to rate it',
                url: rateUrl,
                iconData: Icons.star_rounded,
                context: context,
              ),
              _buildFeedbackSetting(context),
              _buildURLSetting(
                title: 'Help with translation',
                description: 'We can use your language skills',
                url: translationUrl,
                iconData: Icons.translate_rounded,
                context: context,
              ),
            ],
          ),
          SettingsSection(
            title: Text(
              'App',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Theme.of(context).primaryColor,
                fontFamily: context.read<ThemeBloc>().fontFamily,
              ),
            ),
            tiles: <SettingsTile>[
              _buildTrashSetting(context),
              _buildUnfinishedSetting(context),
              _buildBackupSetting(context),
            ],
          ),
          SettingsSection(
            title: Text(
              'Appearance',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Theme.of(context).primaryColor,
                fontFamily: context.read<ThemeBloc>().fontFamily,
              ),
            ),
            tiles: <SettingsTile>[
              _buildAccentSetting(context),
              _buildThemeModeSetting(context),
              _buildFontSetting(context),
              _buildRatingTypeSetting(context),
              _buildOutlinesSetting(context),
              _buildCornersSetting(context),
            ],
          ),
          SettingsSection(
            title: Text(
              'About',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Theme.of(context).primaryColor,
                fontFamily: context.read<ThemeBloc>().fontFamily,
              ),
            ),
            tiles: <SettingsTile>[
              _buildURLSetting(
                title: 'Version',
                description: version,
                url: releaseUrl,
                iconData: FontAwesomeIcons.rocket,
                context: context,
              ),
              _buildURLSetting(
                title: 'Source code',
                description: 'See on Github',
                url: repoUrl,
                iconData: FontAwesomeIcons.code,
                context: context,
              ),
              _buildURLSetting(
                title: 'Changelog',
                description: 'Check what\'s new in Openreads',
                url: releaseUrl,
                iconData: Icons.auto_awesome_rounded,
                context: context,
              ),
              _buildURLSetting(
                title: 'Licence',
                description: 'GNU General Public License v2.0',
                url: licenceUrl,
                iconData: Icons.copyright_rounded,
                context: context,
              ),
            ],
          ),
        ],
      ),
    );
  }
}
